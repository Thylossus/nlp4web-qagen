package tag.cloud.enrichment;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import config.DBConfig;
import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;

public class CategorySearch implements Callable<Result> {

	Wikipedia wiki;
	String searchterm;
	String[] keywords;
	int maxSize = 1000;

	private void init() {
		DBConfig dbConfig = DBConfig.getInstance();
		try {
			wiki = new Wikipedia(dbConfig.getJwplDbConfig());
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

		for (Category category : page.getCategories()) {
			String categoryTitle = category.getTitle().getPlainTitle().toLowerCase();
			boolean check = false;

			// check if category contains at least one of the keywords
			for (String keyword : keywords) {
				if (categoryTitle.contains(keyword.toLowerCase())) {
					check = true;
					break;
				}
			}

			// check size of category
			int size = category.getNumberOfPages();
			if (size > maxSize) {
				check = false;
			}
			
			if (check) {
				categoryIds.add(category.getPageId());
				System.out.println(category.getTitle().getPlainTitle() + " - Size: " + size);
				articleIds.addAll(category.getArticleIds());
			}
		}

		return new Result(categoryIds, articleIds);
	}
}
