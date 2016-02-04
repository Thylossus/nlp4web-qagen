package candidate.extraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiTitleParsingException;
import types.CandidateAnswer;
import types.CorrectAnswer;
import util.UimaListHandler;
import util.WikipediaFactory;

public class CandidateExtraction extends JCasAnnotator_ImplBase {

	/** Max depth for sub-category search */
	public static final String PARAM_CATEGORY_DEPTH = "categoryDepth";
	
	/** Max depth for sub-category search */
	@ConfigurationParameter(
			name = PARAM_CATEGORY_DEPTH,
			defaultValue = "15",
			description = "Max depth for sub-category search")
	private int categoryDepth;
	
	private static final String LF = System.getProperty("line.separator");
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Wikipedia wiki;
		Category cat;
		List<Integer> categories;
		Set<Integer> articles = new HashSet<Integer>();
		Logger logger = this.getContext().getLogger();
		CandidateAnswer candidate;
		StringBuilder sb;
		
		try {
			wiki = WikipediaFactory.getWikipedia(); 
			
			for (CorrectAnswer ca : JCasUtil.select(jcas, CorrectAnswer.class)) {
				categories = UimaListHandler.listToJavaIntegerList(ca.getMostRelevantCategories());
				
				for (int id : categories) {
					cat = wiki.getCategory(id);
					articles.addAll(this.getAllArticles(cat));
				}
				
				// Add candidate answer annotations
				sb = new StringBuilder();
				sb.append("Answer Candidates (");
				sb.append(articles.size());
				sb.append("): ");
				sb.append(LF);
				
				for (int articleid : articles) {
					candidate = new CandidateAnswer(jcas);
					candidate.setBegin(ca.getBegin());
					candidate.setEnd(ca.getEnd());
					candidate.setWikipediaPageId(articleid);
					candidate.setTitle(this.getCleanedTitle(wiki, articleid));
					candidate.addToIndexes();
				
					sb.append(articleid);
					sb.append(" = ");
					sb.append(candidate.getTitle());
					
					sb.append(LF);
				}
				
				this.getContext().getLogger().log(Level.INFO, sb.toString());
			}
			
		} catch (WikiApiException e) {
			logger.log(Level.SEVERE, "Could not load articles form categories. Error: " + e.toString());
		}

	}
	
	/**
	 * Recursively find all articles of a category.
	 * @param cat The category.
	 * @return A set of article ids.
	 * @throws WikiApiException
	 */
	private Set<Integer> getAllArticles(Category cat) throws WikiApiException {
		return getAllArticles(cat, new HashSet<>(), 0);
	}
	
	private Set<Integer> getAllArticles(Category cat, Set<Integer> seenCategoryIDs, int depth) throws WikiApiException {
		Set<Integer> articles = new HashSet<>();
		
		// Add articles of this category
		articles.addAll(cat.getArticleIds());
		
		// Add articles of all child categories
		if (cat.getNumberOfChildren() > 0) {
			for (Category c : cat.getChildren()) {
				if (!seenCategoryIDs.contains(c.getPageId()) && depth < categoryDepth) {
					seenCategoryIDs.add(c.getPageId());
					articles.addAll(this.getAllArticles(c, seenCategoryIDs, depth+1));
				}
			}
		}
		
		return articles;
		
	}
	
	/**
	 * Convert a Wikipedia title into a form that is suitable for using it as a answer candidate.
	 * This is achieved by removing the disambiguation information from the title. 
	 * @param wiki A reference to the Wikipedia API instance.
	 * @param articleid The ID of the candidate answer's Wikipedia article
	 * @return The cleaned title.
	 * @throws WikiTitleParsingException
	 * @throws WikiApiException
	 */
	private String getCleanedTitle(Wikipedia wiki, int articleid) throws WikiTitleParsingException, WikiApiException {
		String title = wiki.getPage(articleid).getTitle().getPlainTitle();
		
		title = title.replaceAll("\\s*\\([^\\)]+\\)\\s*$", "");
		
		return title;
	}

}
