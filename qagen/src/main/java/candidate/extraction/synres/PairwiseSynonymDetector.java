package candidate.extraction.synres;

import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import types.Answer;
import types.CandidateAnswer;
import util.WikipediaFactory;

/**
 * This is the parent class for all available synonym detection mechanisms. By using this pattern, the different algorithms can
 * be encapsulated into single class files and they are accessible through the same interface for the SynonymResolution Engine
 */
public abstract class PairwiseSynonymDetector {

	protected Wikipedia wiki;
	
	public PairwiseSynonymDetector() {
		try {
			wiki = WikipediaFactory.getWikipedia();
		} catch (WikiInitializationException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * This method should be called after every document to cleanup caches etc. that are no longer relevant
	 */
	public void clearContext() {
		
	}
	
	/**
	 * The main method for a synonym detector: Check, whether candidates a and b are synonyms to each other.
	 * @param candidateA First word to check
	 * @param candidateB Second word to check
	 * @return true, if they are synonyms.
	 */
	abstract public boolean isSynonym(JCas jcas, Answer candidateA, Answer candidateB);
	
	/**
	 * Returns the answer text, either the Title attribute for CandidateAnswers or the covered text for everything else
	 * @param a the Answer
	 * @return Text
	 */
	protected String getAnswerText(Answer a) {
		if (a instanceof CandidateAnswer) {
			return ((CandidateAnswer)a).getTitle();
		} else {
			return a.getCoveredText();
		}
	}
	
	/**
	 * Returns the corresponding Wiki API Page Object for the given answer
	 * @param a Answer
	 * @return Page Object
	 * @throws WikiApiException if the object cannot be recovered
	 */
	protected Page getAnswerPage(Answer a) throws WikiApiException {
		if (a instanceof CandidateAnswer) {
			return wiki.getPage(((CandidateAnswer)a).getWikipediaPageId());
		} else {
			return wiki.getPage(a.getCoveredText());
		}
	}
}
