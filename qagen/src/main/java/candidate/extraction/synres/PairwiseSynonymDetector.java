package candidate.extraction.synres;

import org.apache.uima.jcas.JCas;

import types.Answer;

/**
 * This is the parent class for all available synonym detection mechanisms. By using this pattern, the different algorithms can
 * be encapsulated into single class files and they are accessible through the same interface for the SynonymResolution Engine
 */
public abstract class PairwiseSynonymDetector {

	/**
	 * The main method for a synonym detector: Check, whether candidates a and b are synonyms to each other.
	 * @param candidateA First word to check
	 * @param candidateB Second word to check
	 * @return true, if they are synonyms.
	 */
	abstract public boolean isSynonym(JCas jcas, Answer candidateA, Answer candidateB);
	
}
