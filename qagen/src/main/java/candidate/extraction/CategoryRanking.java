package candidate.extraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
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
import types.CorrectAnswer;
import util.PageRankLoader;
import util.UimaListHandler;
import util.WikipediaFactory;

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
		//FIXME: Removed DatabaseConfiguration dbconfig = DBConfig.getJwplDbConfig();
		Wikipedia wiki;
		Category cat;
		List<Integer> categories;
		Logger logger = this.getContext().getLogger();
		List<PageRankTuple> categoryArticleMapping = new LinkedList<>();
		
		try {
			wiki = WikipediaFactory.getWikipedia(); // FIXME: Replaced new Wikipedia(dbconfig);
			
			for (CorrectAnswer ca : JCasUtil.select(jcas, CorrectAnswer.class)) {
				categories = UimaListHandler.listToJavaIntegerList(ca.getCategories());
				
				// Load all articles for each category
				for (int id : categories) {
					cat = wiki.getCategory(id);
					categoryArticleMapping.add(new PageRankTuple(id, this.getAllArticles(cat)));
				}
				
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
		return getAllArticles(cat, new HashSet<>(), 0);
	}

	/**
	 * This method works like {@link #getAllArticles(Category)}, but it stores the categories it has seen, thus it prevents StackOverflowErrors if there are cyclic category relations
	 * @param cat The category 
	 * @param seenCategoryIDs A set of category page ids the search has already seen
	 * @return A set of article ids
	 * @throws WikiApiException if the api fails
	 */
	private Set<Integer> getAllArticles(Category cat, Set<Integer> seenCategoryIDs, int depth) throws WikiApiException {
		Set<Integer> articles = new HashSet<>();
		
		int MAXDEPTH = 15;
		
		// Add articles of this category
		articles.addAll(cat.getArticleIds());
		
		// Add articles of all child categories
		if (cat.getNumberOfChildren() > 0) {
			for (Category c : cat.getChildren()) {
				if (!seenCategoryIDs.contains(c.getPageId()) && depth < MAXDEPTH) {
					seenCategoryIDs.add(c.getPageId());
					articles.addAll(this.getAllArticles(c, seenCategoryIDs, depth+1));
				}
			}
		}
		
		return articles;
		
	}
	
	/**
	 * Selects the most relevant categories from the list of provided categories based on their scores.
	 * The number of selected categories depends on the numCategories parameter (analysis engine parameter).
	 * The categories with the highest scores are selected.
	 * 
	 * @param categories A list of category tuples.
	 * @return The IDs of the most relevant categories according to the measure in {@link PageRankTuple#getScore(PageRankLoader)}.
	 */
	private List<Integer> getMostRelevantCategories(List<PageRankTuple> categories) {
		List<Integer> mostRelevantCategories = new ArrayList<>(this.numCategories);
		final PageRankLoader prl = new PageRankLoader();
		
		// Sort in descending order
		Collections.sort(categories, new Comparator<PageRankTuple>() {

			@Override
			public int compare(PageRankTuple o1, PageRankTuple o2) {
				if (o1.getScore(prl) > o2.getScore(prl)) {
					return -1;
				} else if (o1.getScore(prl) < o2.getScore(prl)) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		prl.closeConnection();
		
		// Select the first n (n = this.numCategories) category IDs
		for (int i = 0; i < this.numCategories; i++) {
			mostRelevantCategories.add(categories.get(i).getCategoryId());
		}
		
		return mostRelevantCategories;
	}
	
	/**
	 * Wrapper for a tuple consisting of a category's id, articles, and score.
	 * The score is calculated based on the articles' page ranks.
	 */
	private class PageRankTuple {
		private final int catId;
		private final Set<Integer> articles;
		private Double score;
		
		public PageRankTuple(int catId, Set<Integer> articles) {
			this.catId = catId;
			this.articles = articles;
			this.score = null;
		}
		
		/**
		 * Retrieve the category's score.
		 * If the score has not been calculated yet, it will be calculated on the fly.
		 * @param prl Reference to the shared page rank loader instance.
		 * @return The category's score.
		 */
		public double getScore(PageRankLoader prl) {
			if (this.score != null) {
				return this.score;
			}
			
			this.score = 0.0;
			
			for (int artId : this.articles) {
				this.score += prl.getPageRank(artId);
			}
			
			// Normalize score to remove bias for categories with lots of articles
			this.score = this.articles.isEmpty() ? 0.0 : this.score / this.articles.size();
			
			return this.score;
		}
		
		public int getCategoryId() {
			return this.catId;
		}
	}
}
