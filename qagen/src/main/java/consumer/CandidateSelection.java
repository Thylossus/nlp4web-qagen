package consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import types.CandidateAnswer;
import types.CorrectAnswer;
import types.Question;

public class CandidateSelection extends JCasConsumer_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		List<String> lines = new ArrayList<>();
		
		Question question = JCasUtil.selectSingle(jcas, Question.class);
		String questionText = question.getCoveredText();
		CorrectAnswer correctAnswer = JCasUtil.selectSingle(jcas, CorrectAnswer.class);
		String correctAnswerText = correctAnswer.getCoveredText();
		
		lines.add(questionText);
		lines.add(correctAnswerText);
		
		List<Score> scores = new ArrayList<Score>();

		for (CandidateAnswer candidateAnswer : JCasUtil.select(jcas, CandidateAnswer.class)) {
			float score = candidateAnswer.getSimilarityScore();
			scores.add(new Score(candidateAnswer, (int) score));
		}

		Collections.sort(scores);
		
		for (int i = 0; i < 3; i++) {
			String answerTitle = scores.get(i).answer.getTitle();
			lines.add(answerTitle);
			System.out.println("Candidate Answer:" + answerTitle);
		}
		
		Path filepath = Paths.get("ressources/results/result.txt");
		try {
			Files.write(filepath, lines);
		} catch (IOException e) {
			this.getContext().getLogger().log(Level.SEVERE, "Could not write output file");
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
