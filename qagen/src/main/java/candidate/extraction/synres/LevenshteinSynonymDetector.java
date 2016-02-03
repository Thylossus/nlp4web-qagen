package candidate.extraction.synres;

import org.apache.uima.jcas.JCas;

import types.Answer;

/**
 *  This Detector uses the Levenshtein Distance between two words. This value measures the amount of transformation steps needed to transform
 *  one word into another. Words that are very close will be treated as synonyms. 
 */
public class LevenshteinSynonymDetector extends PairwiseSynonymDetector {

	/** The minimum number of transformations between two words to recognize them as different */
	private int minDifference;
	
	/**
	 * Creates a new {@link #LevenshteinSynonymDetector()} with the default minimum distance of 3
	 */
	public LevenshteinSynonymDetector() {
		this(3);
	}
	
	/**
	 * Creates a new {@link LevenshteinSynonymDetector} with the minimal difference of <code>minDifference</code> transformation steps between
	 * two words for not being treatet as synonyms
	 * @param minDifference Minimum Difference
	 */
	public LevenshteinSynonymDetector(int minDifference) {
		this.minDifference = minDifference;
	}
	
	@Override
	public boolean isSynonym(JCas jcas, Answer candidateA, Answer candidateB) {
		
		return calculateDifference(getAnswerText(candidateA).toLowerCase(), getAnswerText(candidateB).toLowerCase()) < minDifference;
		
	}
	
	/**
	 * Caculates the minimum amount of steps to transform String a to String b, using the Levenshtein Distance<br />
	 * The implementation follows the algorithm definition from https://de.wikipedia.org/wiki/Levenshtein-Distanz#Algorithmus
	 * @param a First String
	 * @param b Second String
	 * @return Amount of steps (the algorithm may terminate if {@link #minDifference} is reached, then this value is returned.
	 */
	private static int calculateDifference(String a, String b) {
		int[][] mat_d = new int[a.length() + 1][b.length() + 1];
		
		mat_d[0][0] = 0;
		for(int i = 0; i < mat_d.length; i++) {
			mat_d[i][0] = i;
			for(int j = 1; j < mat_d[i].length; j++) {
				if (i == 0) {
					mat_d[i][j] = j;
				} else {
					int val = mat_d[i-1][j-1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1); // Same Character / Replacement
					val = Math.min(val, mat_d[i][j-1] + 1); // Addition
					val = Math.min(val, mat_d[i-1][j] + 1); // Deletion
					mat_d[i][j] = val;
					
				}
			}
		}
		
		return mat_d[a.length()][b.length()];
	}
	
}
