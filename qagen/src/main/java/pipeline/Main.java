/**
 * 
 */
package pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import candidate.extraction.BlacklistFilter;
import candidate.extraction.CandidateExtraction;
import candidate.extraction.CategoryRanking;
import candidate.extraction.DuplicateTitleDetection;
import candidate.extraction.SynonymResolution;
import consumer.CandidateSelection;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import input.OpenTriviaQAParser;
import question.processing.KeywordExtraction;
import similarity.detection.SimilarityDetection;
import tag.cloud.enrichment.CategoryDetection;

/**
 * 
 */
public class Main {
	
	
	

	private int numCats;
	
	private int catDepth;
	
	/** Sets default values */
	public Main() {
		numCats = 2;
		catDepth = 3;
	}
	
	/**
	 * Runs the pipeline
	 * @param inFile Input File
	 * @param outFile Output File
	 * @throws UIMAException UIMA Failure
	 * @throws IOException General IO Failure
	 */
	public void processPipeline(String inFile, String outFile) throws UIMAException, IOException {
		
		// Collection reader
		CollectionReader questionParser = createReader(OpenTriviaQAParser.class, OpenTriviaQAParser.PARAM_INPUT_FILE, inFile);

		// Semantical analysis of the question / answer
		AnalysisEngine segTagger = createEngine(StanfordSegmenter.class);
		AnalysisEngine posTagger = createEngine(StanfordPosTagger.class);
		AnalysisEngine nerTagger = createEngine(StanfordNamedEntityRecognizer.class);
		
		// Finding relevant categories / articles
		AnalysisEngine keywordExtraction = createEngine(KeywordExtraction.class);
		AnalysisEngine correctAnswerCategoryDetection = createEngine(CategoryDetection.class,
				CategoryDetection.PARAM_SEARCH_TYPE, "correctAnswer");
		AnalysisEngine categoryRanking = createEngine(CategoryRanking.class,
				CategoryRanking.PARAM_CATEGORY_DEPTH, catDepth,
				CategoryRanking.PARAM_NUM_CATEGORIES, numCats);
		AnalysisEngine candidateExtraction = createEngine(CandidateExtraction.class,
				CandidateExtraction.PARAM_CATEGORY_DEPTH, catDepth);
		
		// Cleaning to get better results
		AnalysisEngine synonymResolution = createEngine(SynonymResolution.class);
		AnalysisEngine duplicateTitleDetection = createEngine(DuplicateTitleDetection.class);
		AnalysisEngine blacklistFilter = createEngine(BlacklistFilter.class);
		
		// Ranking of the possible answers and selection of the best candidates
		AnalysisEngine candidateAnswerCategoryDetection = createEngine(CategoryDetection.class,
				CategoryDetection.PARAM_SEARCH_TYPE, "candidateAnswer");
		AnalysisEngine similarityDetection = createEngine(SimilarityDetection.class);
		
		// Final selection and output
		AnalysisEngine candidateSelection = createEngine(CandidateSelection.class,
				CandidateSelection.PARAM_OUTPUT_FILE, outFile);
				
		SimplePipeline.runPipeline(
				questionParser,
				segTagger,
				posTagger,
				nerTagger,
				keywordExtraction,
				correctAnswerCategoryDetection,
				categoryRanking,
				candidateExtraction,
				duplicateTitleDetection,
				blacklistFilter,
				synonymResolution,
				candidateAnswerCategoryDetection,
				similarityDetection,
				candidateSelection);
	}

	public void setNumCats(int numCats) {
		this.numCats = numCats;
	}

	public void setCatDepth(int catDepth) {
		this.catDepth = catDepth;
	}
	
	public static void main(String[] args) throws UIMAException, IOException {
		Main main = new Main();
		
		List<String> inputFiles = new LinkedList<>();
		
		for(String arg : args) {
			if (arg.startsWith("-catDepth=")) {
				main.setCatDepth(Integer.parseInt(arg.substring("-catDepth=".length())));
			} else if (arg.startsWith("-numCats=")) {
				main.setCatDepth(Integer.parseInt(arg.substring("-numCats=".length())));
			} else {
				inputFiles.add(arg);
			}
		}
		
		// If no arguments are specified prompt as it is easier for testing
		if (inputFiles.size() == 0) {
			try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {}
			
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
				inputFiles.add(fileChooser.getSelectedFile().getAbsolutePath());
			} else {
				System.exit(0);
			}

			Object catDepth = JOptionPane.showInputDialog(null, "Select depth for category search", "Parameter",
					JOptionPane.QUESTION_MESSAGE, null, new Integer[]{1,2,3,4,5,6,7,8,9,10}, 3);
			if (catDepth != null) main.setCatDepth((Integer)catDepth); else System.exit(0);

			Object numCat = JOptionPane.showInputDialog(null, "Select number of relevant categories", "Parameter",
					JOptionPane.QUESTION_MESSAGE, null, new Integer[]{1,2,3,4,5}, 2);
			if (numCat != null) main.setNumCats((Integer)numCat); else System.exit(0);
		}
		
		for(String inputFile : inputFiles) {
			String outputFile = inputFile + ".out";
			main.processPipeline(inputFile, outputFile);
		}
	}
}
