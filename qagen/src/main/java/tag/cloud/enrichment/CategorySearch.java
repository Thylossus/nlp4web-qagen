package tag.cloud.enrichment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import edu.stanford.nlp.util.Pair;
import util.WikipediaFactory;

public class CategorySearch implements Callable<Result> {

	Wikipedia wiki;
	String searchterm;
	String[] keywords;
	private final int MAX_CATEGORY_SIZE = 1000;
	private final double CATEGORY_SPLIT = 0.2;

	private void init() {
		try {
			wiki = WikipediaFactory.getWikipedia();
		} catch (WikiInitializationException e) {
			System.err.println("Could not create the wikipedia object. Error: " + e.toString());
		}
	}

	public CategorySearch(String searchterm, String[] keywords) {
		init();
		this.searchterm = searchterm;
		this.keywords = keywords;
	}

	public CategorySearch(String searchterm) {
		init();
		this.searchterm = searchterm;
	}

	public Result call() throws Exception {
		Page page = wiki.getPage(searchterm);
		Set<Integer> categoryIds = new TreeSet<Integer>();
		Set<Integer> articleIds = new TreeSet<Integer>();

		List<Pair<Integer, Integer>> categoryRatings = new ArrayList<Pair<Integer, Integer>>();

		Set<Category> categories = page.getCategories();
		
		if (this.isDisambiguationPage(page)) {
			System.out.println("Found a disambiguation page (" + this.searchterm + ")");
			categories = this.findCategoriesOfCorrectPage(page);
		}
		
		for (Category category : categories) {
			int size = category.getNumberOfPages();
			if (size <= MAX_CATEGORY_SIZE) {
				int rating = 0;
				String categoryTitle = category.getTitle().getPlainTitle().toLowerCase();

				for (String keyword : keywords) {
					if (categoryTitle.contains(keyword.toLowerCase())) {
						rating++;
					}
				}

				if (rating == 0) {
					rating = -size;
				}

				categoryRatings.add(new Pair<Integer, Integer>(category.getPageId(), rating));
			}
		}

		Comparator<Pair<Integer, Integer>> c = new Comparator<Pair<Integer, Integer>>() {
			@Override
			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
				return -Integer.compare(o1.second(), o2.second());
			}
		};
		categoryRatings.sort(c);

		Iterator<Pair<Integer, Integer>> it = categoryRatings.iterator();
		while (it.hasNext()) {
			Pair<Integer, Integer> pair = it.next();
			if (pair.second() > 0 || (categoryIds.size() <= Math.floor(categoryRatings.size() * CATEGORY_SPLIT))) {
				Category category = wiki.getCategory(pair.first());
				categoryIds.add(pair.first());
				articleIds.addAll(category.getArticleIds());
				System.out.println(category.getTitle().getPlainTitle());
			}
		}

		return new Result(categoryIds, articleIds);
	}
	
	/**
	 * Check if the provided page is a disambiguation page because the JWPL API is unreliable in this regard.
	 * @param page The page to check.
	 * @return
	 */
	private boolean isDisambiguationPage(Page page) {
		Pattern patternDisamb = Pattern.compile("\\{\\{[^\\}]*([dD]isambiguation|[Hh]ndis|[gG]eodis|\\-dis)(\\|[^\\}]+){0,1}\\}\\}");
		Matcher m = patternDisamb.matcher(page.getText());
		return m.find();
	}
	
	/**
	 * From a disambiguation page find the most likely page based on the questions keywords.
	 * @param disambiguationPage The disambiguation page.
	 * @return The categories for the found page.
	 * @throws WikiApiException
	 */
	private Set<Category> findCategoriesOfCorrectPage(Page disambiguationPage) throws WikiApiException {
		Set<Page> outlinks = disambiguationPage.getOutlinks();
		String pagePlainText;
		int score;
		Page maxPage = null;
		int maxScore = -1;
		
		for (Page candidate : outlinks) {
			pagePlainText = candidate.getPlainText();
			score = 0;
			
			for(String keyword : this.keywords) {
				score += StringUtils.countMatches(pagePlainText, keyword);
			}
			
			if (score > maxScore) {
				maxScore = score;
				maxPage = candidate;
			}
			
			System.out.println("Disambiguation candidate '" + candidate.getTitle().getPlainTitle() + "' has score of " + score);
		}
		
		System.out.println("Candidate with the highest score is " + maxPage.getTitle().getPlainTitle() + " with a score of " + maxScore);
		
		return maxPage.getCategories();
	}
}
