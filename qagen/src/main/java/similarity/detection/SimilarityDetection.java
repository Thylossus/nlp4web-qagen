package similarity.detection;

import java.util.Iterator;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import types.CandidateAnswer;
import types.CorrectAnswer;
import util.UimaListHandler;

public class SimilarityDetection extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		
		for (CorrectAnswer correctAnswer : JCasUtil.select(jcas, CorrectAnswer.class)) {
			List<Integer> correctAnswerCategories = UimaListHandler.listToJavaIntegerList(correctAnswer.getCategories());

			for (CandidateAnswer candidateAnswer : JCasUtil.select(jcas, CandidateAnswer.class)) {
				List<Integer> candidateAnswerCategories = UimaListHandler.listToJavaIntegerList(candidateAnswer.getCategories());
		
				int similarity = 0;
				Iterator<Integer> it = correctAnswerCategories.iterator();
				while(it.hasNext()){
					Integer category = it.next();
					if(candidateAnswerCategories.contains(category)){
						similarity++;
					}
				}
				candidateAnswer.setSimilarityScore(similarity);
			}
		}	
	}
}
