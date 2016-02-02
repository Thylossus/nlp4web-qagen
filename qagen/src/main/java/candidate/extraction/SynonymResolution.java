package candidate.extraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import candidate.extraction.synres.LevenshteinSynonymDetector;
import candidate.extraction.synres.PairwiseSynonymDetector;
import candidate.extraction.synres.SameStringSynonymDetector;
import candidate.extraction.synres.WikiRedirectPageSynonymDetector;
import types.Answer;
import types.CandidateAnswer;
import types.CorrectAnswer;

/**
 * This engine tries to identify synonymes within the answer candidates and to eliminate them.
 * <br />
 * The techniques used are (applyed pairwise):
 * <ul>
 * <li>String comparison</li>
 * <li>Levensthein distance</li>
 * <li>Lookup of Wikipedia redirect pages</li>
 * </ul>
 */
public class SynonymResolution extends JCasAnnotator_ImplBase {

	/** Minimal Levenshtein Distance between two Answer that is needed not to treat them as synonyms */
	public static final String PARAM_LEV_MIN_DISTANCE = "levenshteinMinDistance";
	
	/** Configures the minimum Levenshtein distance, as described in {@link LevenshteinSynonymDetector} */
	@ConfigurationParameter(
			name = PARAM_LEV_MIN_DISTANCE,
			mandatory = false,
			description = "Minimal Levenshtein Distance between two Answer that is needed not to treat them as synonyms",
			defaultValue = "3")
	private String minDistStr;
	
	/** This List contains all {@link PairwiseSynonymDetector}s in the order they should be applied */
	private List<PairwiseSynonymDetector> appliedDetectors;
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		
		int minDist = 3;
		
		try {
			minDist = Integer.parseInt(minDistStr);
		} catch (NumberFormatException ex) {
			System.err.println("Could not parse argument for minimum Levenshtein Distance, falling back to default value (" + minDist + ").");
			ex.printStackTrace();
		}
		
		// Use the following detectors to find synonyms. The are ordered from low to high performance impact 
		appliedDetectors = new LinkedList<>();
		appliedDetectors.add(new SameStringSynonymDetector());         // Check for String equality
		appliedDetectors.add(new LevenshteinSynonymDetector(minDist)); // Use the Levenshtein Distance
		appliedDetectors.add(new WikiRedirectPageSynonymDetector());   // Check for Wikipedia Redirect Pages
		
	}
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		// Preparation: Get correct answer and candidates
		CorrectAnswer correctAnswer = getCorrectAnswer(jcas);
		List<CandidateAnswer> candidateAnswers = new ArrayList<>(JCasUtil.select(jcas, CandidateAnswer.class));
		
		// Step 1: Check all AnswerCandidates against the correct Answer. If a synonym is detected here, obviously the candidate has to be discarded
		for(CandidateAnswer candidateAnswer : candidateAnswers) {
			if (isSynonym(jcas, correctAnswer, candidateAnswer)) {
				System.out.println("SYNONYM DETECTED! " + correctAnswer.getCoveredText() + " -- " + candidateAnswer.getCoveredText());
			}
		}
		
		// Step 2: Check all remaining AnswerCandidates against each other
		
		// TODO
		
	}
	
	/**
	 * Returns the correct answer from the CAS. Or throws an exception if it can't be found
	 * @param jcas the CAS
	 * @return Correct answer
	 */
	private CorrectAnswer getCorrectAnswer(JCas jcas) {
		Collection<CorrectAnswer> cAnswer = JCasUtil.select(jcas, CorrectAnswer.class);
		if (cAnswer == null || cAnswer.isEmpty()) {
			throw new IllegalArgumentException("The CAS does not contain a CorrectAnswer");
		} else if (cAnswer.size() > 1) {
			throw new IllegalArgumentException("The CAS contains more than one CorrectAnswer");
		}
		return cAnswer.iterator().next();
	}
	
	/**
	 * Applies all Detectors defined in {@link #appliedDetectors} to check for synonyms
	 * @param jcas the CAS
	 * @param candidateA First candidate for the check
	 * @param candidateB Second candidate for the check
	 * @return true, if both candidates are synonyms, false otherwise
	 */
	private boolean isSynonym(JCas jcas, Answer candidateA, Answer candidateB) {
		for(PairwiseSynonymDetector detector : appliedDetectors) {
			if (detector.isSynonym(jcas, candidateA, candidateB)) {
				return true;
			}
		}
		return false;
	}
	
}
