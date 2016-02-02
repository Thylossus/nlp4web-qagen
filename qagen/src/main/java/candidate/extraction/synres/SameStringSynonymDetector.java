package candidate.extraction.synres;

import org.apache.uima.jcas.JCas;

import types.Answer;

/**
 * This class uses a String comparison to determine whether the candidates are synonyms
 */
public class SameStringSynonymDetector extends PairwiseSynonymDetector {

	@Override
	public boolean isSynonym(JCas jcas, Answer candidateA, Answer candidateB) {
		
		return candidateA.getCoveredText().toLowerCase().equals(candidateB.getCoveredText().toLowerCase());
	}

}
