package util;
import config.DBConfig;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;

/**
 *  Singleton-Factory for the {@link Wikipedia}-Object, that can be accessed by multiple worker threads simultaneously
 */
public class WikipediaFactory {

	/** The singleton instance */
	private static Wikipedia wikipedia;
	
	/**
	 * Creates the one and only {@link Wikipedia} Object and returns it to the caller
	 * @return Wikipedia singleton instance
	 * @throws WikiInitializationException if creation of the Object fails
	 */
	public synchronized static Wikipedia getWikipedia() throws WikiInitializationException{
		if (wikipedia == null) {
			wikipedia = new Wikipedia(DBConfig.getJwplDbConfig());
		}
		return wikipedia;
	}
	
}
