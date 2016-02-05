package consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import types.CandidateAnswer;
import types.CorrectAnswer;
import types.Question;

public class CandidateSelection extends JCasConsumer_ImplBase {

	/** Defines the path of the output file */
	public static final String PARAM_OUTPUT_FILE = "outputFile";
	
	/** Add additional Header Messages to the Output file */
	public static final String PARAM_LOG = "logmsg";
	
	/** The file the output is written into */
	@ConfigurationParameter(
			name = PARAM_LOG,
			mandatory = false,
			description = "Add additional Header Messages to the Output file",
			defaultValue = "")
	private String logmsg;
	
	/** The file the output is written into */
	@ConfigurationParameter(
			name = PARAM_OUTPUT_FILE,
			mandatory = false,
			description = "The file the output is written into",
			defaultValue = "src/main/resources/questions/results/result.txt")
	private String outputFile;

	/** The Path the output is written into. It's deleted in every initialization phase */
	private Path outputFilePath;
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		
		outputFilePath = Paths.get(outputFile);
		
		try {
			// Check if directory exists
			Path parentDir = outputFilePath.getParent();
			if (!Files.exists(parentDir)) {
				Files.createDirectories(parentDir);
			}
			
			Files.deleteIfExists(outputFilePath);
			Files.createFile(outputFilePath);
			
			// If configured, add a header to the output
			List<String> header = new LinkedList<>();
			if (logmsg.trim().length() > 0) {
				String[] headerLines = logmsg.split("\n");
				for(String line : headerLines) header.add("! " + line);
				header.add("");
			}
			
			Files.write(outputFilePath, header, StandardOpenOption.APPEND);
		} catch (IOException ex) {
			throw new ResourceInitializationException(ex);
		}
	}
	
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
		
		try {
			Files.write(outputFilePath, lines, StandardOpenOption.APPEND);
		} catch (IOException e) {
			this.getContext().getLogger().log(Level.SEVERE, "Could not write output file", e);
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
