package candidate.extraction.synres;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import types.Answer;

public class WikiRedirectPageSynonymDetector extends PairwiseSynonymDetector {

	
	@Override
	public boolean isSynonym(JCas jcas, Answer candidateA, Answer candidateB) {
		try {
			Set<String> redirectsA = getRedirects(candidateA);
			Set<String> redirectsB = getRedirects(candidateB);
			
			// First Step: Check whether one page redirects to the other
			if (redirectsA.contains(getWikiPageTitle(candidateB)) || redirectsB.contains(getWikiPageTitle(candidateA))) {
				return true;
			}
			
			// Second Step: Check for Common redirection targets
			for(String target : redirectsA) {
				if (redirectsB.contains(target)) return true;
			}
			
			return false;
		} catch (WikiApiException ex) {
			System.err.println("Could not get WIKI PAGE: " + ex.getMessage());
			return false;
		}
	}
	
	// The following Section caches redirects and Wikipeda Page Titles
	
	private Map<String, Set<String>> redirects = new HashMap<>();
	private Map<String, String> wikiPageTitles = new HashMap<>();

	@Override
	public void clearContext() {
		super.clearContext();
		redirects.clear();
	}
	
	private Set<String> getRedirects(Answer answer) throws WikiApiException {
		assureCache(answer);
		return redirects.get(getAnswerText(answer));
	}
	
	private String getWikiPageTitle(Answer answer) throws WikiApiException {
		assureCache(answer);
		return wikiPageTitles.get(getAnswerText(answer));
	}
	
	private void assureCache(Answer answer) throws WikiApiException {
		String key = getAnswerText(answer);
		Page pageB = getAnswerPage(answer);
		redirects.put(key, pageB.getRedirects());
		wikiPageTitles.put(key, pageB.getTitle().getWikiStyleTitle());
	}
	
}
