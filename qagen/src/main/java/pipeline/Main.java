/**
 * 
 */
package pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import candidate.extraction.CandidateExtraction;
import candidate.extraction.CategoryRanking;
import candidate.extraction.SynonymResolution;
import consumer.CandidateSelection;
import input.OpenTriviaQAParser;
import question.processing.KeywordExtraction;
import similarity.detection.SimilarityDetection;
import tag.cloud.enrichment.CategoryDetection;

/**
 * 
 */
public class Main {
	
	public static void main(String[] args) throws UIMAException, IOException {
		CollectionReader questionParser = createReader(OpenTriviaQAParser.class);
		
		AnalysisEngine keywordExtraction = createEngine(KeywordExtraction.class);
		AnalysisEngine correctAnswerCategoryDetection = createEngine(CategoryDetection.class, CategoryDetection.PARAM_SEARCH_TYPE, "correctAnswer");
		AnalysisEngine categoryRanking = createEngine(CategoryRanking.class);
		AnalysisEngine candidateExtraction = createEngine(CandidateExtraction.class);
		AnalysisEngine synonymResolution = createEngine(SynonymResolution.class);
		AnalysisEngine candidateAnswerCategoryDetection = createEngine(CategoryDetection.class, CategoryDetection.PARAM_SEARCH_TYPE, "candidateAnswer");
		AnalysisEngine similarityDetection = createEngine(SimilarityDetection.class);
		AnalysisEngine candidateSelection = createEngine(CandidateSelection.class);
				
		SimplePipeline.runPipeline(
				questionParser,
				keywordExtraction,
				correctAnswerCategoryDetection,
				categoryRanking,
				candidateExtraction,
				synonymResolution,
				candidateAnswerCategoryDetection,
				similarityDetection,
				candidateSelection);
		
	}
	
}
