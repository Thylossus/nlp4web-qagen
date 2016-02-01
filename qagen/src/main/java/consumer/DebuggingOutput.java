package consumer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import types.CandidateAnswer;

public class DebuggingOutput extends JCasConsumer_ImplBase {

	private static final String LF = System.getProperty("line.separator");

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();

		// Load document information
		sb.append("=== CAS ===");
		sb.append(LF);
		sb.append("-- Document Text --");
		sb.append(LF);
		sb.append(jcas.getDocumentText());
		sb.append(LF);
		sb.append("-- Annotations --");
		sb.append(LF);

		// Iterate over all annotations
		for (Annotation a : JCasUtil.select(jcas, Annotation.class)) {
			if (a instanceof CandidateAnswer) {
				//do nothing
			} else {
				sb.append("[" + a.getType().getShortName() + "] ");
				sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
				sb.append(a.getCoveredText());
				sb.append(LF);
			}
		}

		sb.append(LF);

		// Log the constructed string
		this.getContext().getLogger().log(Level.INFO, sb.toString());
	}

}
