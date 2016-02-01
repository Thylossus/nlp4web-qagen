package consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import config.DBConfig;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import types.CandidateAnswer;

public class CandidateSelection extends JCasConsumer_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		List<Score> scores = new ArrayList<Score>();

		for (CandidateAnswer candidateAnswer : JCasUtil.select(jcas, CandidateAnswer.class)) {
			float score = candidateAnswer.getSimilarityScore();
			scores.add(new Score(candidateAnswer, (int) score));
		}

		Collections.sort(scores);

		Wikipedia wiki;
		try {
			wiki = new Wikipedia(DBConfig.getJwplDbConfig());
			for (int i = 0; i < 3; i++) {
				int answerID = scores.get(i).answer.getWikipediaPageId();
				Page answer = wiki.getPage(answerID);
				System.out.println("Candidate Answer:" + answer.getTitle());
			}
		} catch (WikiApiException e) {

		}
	}

}

class Score implements Comparable<Score> {
	int score;
	CandidateAnswer answer;

	public Score(CandidateAnswer answer, int score) {
		this.score = score;
		this.answer = answer;
	}

	@Override
	public int compareTo(Score o) {
		return score < o.score ? -1 : score > o.score ? 1 : 0;
	}
}
