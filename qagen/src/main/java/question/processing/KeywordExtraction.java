package question.processing;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.NonEmptyStringList;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NP;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import types.CorrectAnswer;
import types.Question;
import util.UimaListHandler;

public class KeywordExtraction extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Set<String> keywords = new HashSet<>();
		
		// Gather all keywords (use set to prevent saving duplicates)
		for (Question question : JCasUtil.select(jcas, Question.class)) {
			
			for (NP np : JCasUtil.selectCovered(jcas, NP.class, question)) {
				keywords.add(np.getCoveredText());
			}
			
			for (NamedEntity ne : JCasUtil.selectCovered(jcas, NamedEntity.class, question)) {
				keywords.add(ne.getCoveredText());
			}
		}
		
		// Create UIMA list from gathered keywords
		NonEmptyStringList neslKeywords = UimaListHandler.stringCollectionToList(jcas, keywords);
		
		// Add feature to correct answer annotation
		for (CorrectAnswer ca : JCasUtil.select(jcas, CorrectAnswer.class)) {
			ca.setKeywords(neslKeywords);
		}
		
//		System.out.println(UimaListHandler.listToString(neslKeywords));
		
	}
	

}
