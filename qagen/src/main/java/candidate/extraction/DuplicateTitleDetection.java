package candidate.extraction;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import types.CandidateAnswer;

/**
 * This analysis engine removes candidates with the same title from the index.
 * Due to disambiguation, it is possible to have two candidates with different article IDs but the same title.
 */
public class DuplicateTitleDetection extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Collection<CandidateAnswer> candidates = JCasUtil.select(jcas, CandidateAnswer.class);
		HashMap<String, Boolean> uniqueCandidates = new HashMap<>(candidates.size());
		List<CandidateAnswer> remove = new LinkedList<>();
		
		for (CandidateAnswer candidate : candidates) {
			if (uniqueCandidates.containsKey(candidate.getTitle())) {
				this.getContext().getLogger().log(Level.INFO, "Removing candidate " + candidate.getTitle() + "(" + candidate.getWikipediaPageId() + ") from index because it's a duplicate.");
				remove.add(candidate);
			} else {
				uniqueCandidates.put(candidate.getTitle(), true);
			}
		}
		
		for (CandidateAnswer rc : remove) {
			rc.removeFromIndexes();
		}
	}

}
