package tag.cloud.enrichment;

public class SearchFactory {
	public static Search[] getSearches(String[] keywords) {
		Search[] searches = new Search[keywords.length * 2];
		// TODO: start two searches for each keyword, one category and one hypernym search
		// TODO: return an array of searches
		return searches;
	}
}
