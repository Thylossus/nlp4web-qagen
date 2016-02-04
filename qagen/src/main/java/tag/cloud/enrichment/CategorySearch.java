package tag.cloud.enrichment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import edu.stanford.nlp.util.Pair;
import util.WikipediaFactory;

public class CategorySearch implements Callable<Result> {

	Wikipedia wiki;
	String searchterm;
	String[] keywords;

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

		for (Category category : page.getCategories()) {
			int rating = 0;
			String categoryTitle = category.getTitle().getPlainTitle().toLowerCase();

			for (String keyword : keywords) {
				if (categoryTitle.contains(keyword.toLowerCase())) {
					rating++;
				}
			}

			if (rating == 0) {
				rating = -category.getNumberOfPages();
			}

			categoryRatings.add(new Pair<Integer, Integer>(category.getPageId(), rating));
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
			if (pair.second() > 0 || (categoryIds.size() <= Math.floor(categoryRatings.size() * 0.2))) {
				Category category = wiki.getCategory(pair.first());
				categoryIds.add(pair.first());
				articleIds.addAll(category.getArticleIds());
				System.out.println(category.getTitle().getPlainTitle());
			}
		}
		
		return new Result(categoryIds, articleIds);
	}
}
