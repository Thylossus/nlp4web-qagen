package pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

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
import consumer.DebuggingOutput;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import input.OpenTriviaQAParser;
import question.processing.KeywordExtraction;
import similarity.detection.SimilarityDetection;
import tag.cloud.enrichment.CategoryDetection;

public class TestPipeline {

	public static void main(String[] args) throws UIMAException, IOException {
		CollectionReader questionParser = createReader(OpenTriviaQAParser.class, OpenTriviaQAParser.PARAM_INPUT_FILE, "questions/questions-by-status/working-questions.txt");
		
		AnalysisEngine segTagger = createEngine(StanfordSegmenter.class);
		AnalysisEngine posTagger = createEngine(StanfordPosTagger.class);
		AnalysisEngine nerTagger = createEngine(StanfordNamedEntityRecognizer.class);
		AnalysisEngine keywordExtraction = createEngine(KeywordExtraction.class);
		AnalysisEngine output = createEngine(DebuggingOutput.class);
		AnalysisEngine correctAnswerCategoryDetection = createEngine(CategoryDetection.class, CategoryDetection.PARAM_SEARCH_TYPE, "correctAnswer");
		AnalysisEngine categoryRanking = createEngine(CategoryRanking.class, CategoryRanking.PARAM_NUM_CATEGORIES, 3);
		AnalysisEngine candidateExtraction = createEngine(CandidateExtraction.class);
		AnalysisEngine duplicateTitleDetection = createEngine(DuplicateTitleDetection.class);
		AnalysisEngine blacklistFilter = createEngine(BlacklistFilter.class);
		AnalysisEngine synonymResolution = createEngine(SynonymResolution.class);
		AnalysisEngine candidateAnswerCategoryDetection = createEngine(CategoryDetection.class, CategoryDetection.PARAM_SEARCH_TYPE, "candidateAnswer");
		AnalysisEngine similarityDetection = createEngine(SimilarityDetection.class);
		AnalysisEngine candidateSelection = createEngine(CandidateSelection.class);
		
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
				candidateSelection,
				output
		);
	}

}
