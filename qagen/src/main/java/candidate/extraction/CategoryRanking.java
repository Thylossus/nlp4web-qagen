package candidate.extraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import config.DBConfig;
import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import types.CorrectAnswer;
import util.UimaListHandler;

public class CategoryRanking extends JCasAnnotator_ImplBase {

	/** Parameter name for the number of relevant categories to extract */
	public static final String PARAM_NUM_CATEGORIES = "Number of relevant categories";
	
	/** Stores the name of the file that the questions are read from */
	@ConfigurationParameter(
			name = PARAM_NUM_CATEGORIES,
			defaultValue = "5",
			description = "Number of relevant categories")
	private int numCategories;
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		DatabaseConfiguration dbconfig = DBConfig.getJwplDbConfig();
		Wikipedia wiki;
		Category cat;
		List<Integer> categories;
		Set<Integer> articles = new HashSet<>();
		Logger logger = this.getContext().getLogger();
		List<PageRankTuple> categoryArticleMapping = new LinkedList<>();
		
		try {
			wiki = new Wikipedia(dbconfig);
			
			for (CorrectAnswer ca : JCasUtil.select(jcas, CorrectAnswer.class)) {
				categories = UimaListHandler.listToJavaIntegerList(ca.getCategories());
				
				// Load all articles for each category
				for (int id : categories) {
					cat = wiki.getCategory(id);
					categoryArticleMapping.add(new PageRankTuple(id, this.getAllArticles(cat)));
				}
				
				// TODO: calculate score
				
				// Update annotation
				ca.setMostRelevantCategories(UimaListHandler.integerCollectionToList(jcas, this.getMostRelevantCategories(categoryArticleMapping)));
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
		Set<Integer> articles = new HashSet<>();
		
		// Add articles of this category
		articles.addAll(cat.getArticleIds());
		
		// Add articles of all child categories
		if (cat.getNumberOfChildren() > 0) {
			for (Category c : cat.getChildren()) {
				articles.addAll(this.getAllArticles(c));
			}
		}
		
		return articles;
	}
	
	private List<Integer> getMostRelevantCategories(List<PageRankTuple> categories) {
		List<Integer> mostRelevantCategories = new ArrayList<>(this.numCategories);
		
		Collections.sort(categories, new Comparator<PageRankTuple>() {

			@Override
			public int compare(PageRankTuple o1, PageRankTuple o2) {
				if (o1.getScore() > o2.getScore()) {
					return 1;
				} else if (o1.getScore() < o2.getScore()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		for (int i = 0; i < this.numCategories; i++) {
			mostRelevantCategories.add(categories.get(i).getCategoryId());
		}
		
		return mostRelevantCategories;
	}
	
	private class PageRankTuple {
		private final int catId;
		private final Set<Integer> articles;
		private Double score;
		
		public PageRankTuple(int catId, Set<Integer> articles) {
			this.catId = catId;
			this.articles = articles;
			this.score = null;
		}
		
		public double getScore() {
			if (this.score != null) {
				return this.score;
			}
			
			// TODO: calculate the score
			
			return this.score;
		}
		
		public int getCategoryId() {
			return this.catId;
		}
	}
}
