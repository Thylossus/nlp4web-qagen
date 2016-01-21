package input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import types.CorrectAnswer;
import types.Question;

/**
 *  This parser takes OpenTriviaQA question files and parses them, one question per document.
 *  <br />
 *  The whole file will be read when this parser is initialized because we assume that the question sets will be rather small.
 *  <br />
 *  The input should follow this structure:
 *  <pre>
 *  # Which answer should you choose?
 *  ^ The correct answer
 *  A This wrong answer
 *  B The correct answer
 *  C Another wrong answer
 *  D A completely wrong answer
 *  </pre>
 *  In this example, the question is placed in the first line after the # character and answer B would be the correct one. All
 *  other answers are ignored because they aren't used as input data.
 *  <br />
 *  Two question sets should be separated by at least one empty (only whitespace) line.
 *  <br />
 *  The file that is read by this parser can and must be specified using the {@link #PARAM_INPUT_FILE} parameter. 
 */
public class OpenTriviaQAParser extends JCasCollectionReader_ImplBase {

	/** Parameter name for the article css selector */
	public static final String PARAM_INPUT_FILE = "inputFile";
	
	/** Stores the name of the file that the questions are read from */
	@ConfigurationParameter(
			name = PARAM_INPUT_FILE,
			mandatory = true,
			description = "Stored the name of the file that the questions are read from")
	private String inputFile;
	
	/** Raw question Texts. They get parsed when the input file is read */
	private List<ParsedQuestion> questions;
	
	/** The amount of all questions in the input file. Used to calculate progress */
	private int questionCount;
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		questions = new LinkedList<ParsedQuestion>();
		questionCount = 0;

		// FIXME: Mit Java 8 könnte man hier try-with-resources benutzen
		try {
			InputStream inStream = getClass().getClassLoader().getResourceAsStream(inputFile);
			parse(inStream);
		} catch (IOException ex) {
			throw new ResourceInitializationException(new IOException("Could not read OpenTrivia QA input file", ex));
		} catch (ParseException ex) {
			throw new ResourceInitializationException(new IOException("Error Parsing OpenTrivia QA input file", ex));
		}
	}
	
	/**
	 * Fills {@link #questions} and {@link #questionCount} with the input read from <code>inStream</code>.
	 * @param inStream The {@link InputStream} that will be read. UTF-8 will be used as charset.
	 * @throws IOException If parsing fails due to technical errors
	 * @throws ParseException If parsing fails due to an invalid input file
	 */
	private void parse(InputStream inStream) throws IOException, ParseException {
		BufferedReader importReader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));

		// Holds the data for the current question
		String importQuestionText = null;
		String importCorrectAnswer = null;
		List<String> importAnswers = new LinkedList<String>();
		
		// The offset, mainly used to generate useful error messages
		int offset = 0;
		
		// Those booleans define whether the first or last line of a questions has been read
		boolean questionStarted = false;
		boolean questionFinished = false;
		while(!questionFinished) {
			// Read next line
			String line = importReader.readLine();
			
			// End of file
			if (line == null) {
				questionFinished = false;
				break;
			}

			offset+=line.length();
			line = line.trim();
			if (line.length() > 0) {
				questionStarted = true;
				
				// Check the different line types. They can be separated by the first characters
				if(line.startsWith("#Q ") && importQuestionText == null) {
					importQuestionText = line.substring("#Q ".length());
				} else if (line.startsWith("^ ")) {
					importCorrectAnswer = line.substring("^ ".length());
				} else if (line.matches("[A-D] .*")) {
					importAnswers.add(line.substring(2));
				}
			} else {
				// This branch skips leading whitespace. Only if at least one "real" line has been read, the loop will quit
				if (questionStarted) {
					questionFinished = true;
				}
			}
		}
		
		if (importQuestionText != null && importCorrectAnswer != null && importAnswers.size() == 4 && importAnswers.contains(importCorrectAnswer)) {
			
			importAnswers.remove(importCorrectAnswer);
			
			// Add the question to the list
			ParsedQuestion question = new ParsedQuestion(importQuestionText, importCorrectAnswer);
			questions.add(question);
			
		} else {
			throw new ParseException("Error parsing question file", offset);
		}
			
		importReader.close();
	}
	
	//FIXME: Erklär mir mal einer, warum das einkommentieren hier Fehler auslöst. Die Deklaration stammt aus dem Interface BaseCollectionReader @Override
	public Progress[] getProgress() {
		// The stored question amount can be used to calculate the progress
		return new Progress[] {
				new ProgressImpl(questionCount, questions.size(), Progress.ENTITIES)
		};
	}

	//FIXME: Erklär mir mal einer, warum das einkommentieren hier Fehler auslöst. Die Deklaration stammt aus dem Interface BaseCollectionReader @Override
	public boolean hasNext() throws IOException, CollectionException {
		// If the collection is empty, there are no more questions left
		return !questions.isEmpty();
	}

	@Override
	public void getNext(JCas jcas) throws IOException, CollectionException {
		if (!hasNext()) {
			throw new IllegalStateException("No more questions left");
		} else {
			// Fetch new document and shorten the list
			ParsedQuestion question = questions.remove(0);
			jcas.setDocumentText(question.getDocumentText());
			jcas.setDocumentLanguage("en");
			
			// Add the question annotation
			Question questionAnnotation = new Question(jcas);
            questionAnnotation.setBegin(question.getQuestionIndex()[0]);
            questionAnnotation.setEnd(question.getQuestionIndex()[1]);
            questionAnnotation.addToIndexes();
            
            // Add answer annotation
            CorrectAnswer answerAnnotation = new CorrectAnswer(jcas);
            answerAnnotation.setBegin(question.getAnswerIndex()[0]);
            answerAnnotation.setEnd(question.getAnswerIndex()[1]);
            answerAnnotation.addToIndexes();
		}
	}

	/**
	 *  Helper class that stores a question that has been parsed during the initialization phase.
	 */
	private class ParsedQuestion {
		
		/** Question text */
		private String question;
		
		/** Correct answer to the question */
		private String correctAnswer;
		
		/** Creates a new Question */
		public ParsedQuestion(String question, String correctAnswer) {
			this.question = question;
			this.correctAnswer = correctAnswer;
		}
		
		/** Returns the document text. See also {@link #getQuestionIndex()} and {@link #getAnswerIndex()} */
		public String getDocumentText() {
			return question + " " + correctAnswer;
		}

		/** Returns the index of the question in {@link getDocumentText()} as an array: [beginIndex, endIndex] */
		public int[] getQuestionIndex() {
			return new int[] {0, question.length()};
		}
		
		/** Returns the index of the answer in {@link getDocumentText()} as an array: [beginIndex, endIndex] */
		public int[] getAnswerIndex() {
			return new int[] {question.length() + 1, question.length() + correctAnswer.length() + 1};
		}
	}
}
