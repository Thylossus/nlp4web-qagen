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
import tag.cloud.enrichment.CategoryAndHypernymDetection;

/**
 * 
 */
public class Main {
	
	public static void main(String[] args) throws UIMAException, IOException {
		CollectionReader questionParser = createReader(OpenTriviaQAParser.class);
		
		AnalysisEngine keywordExtraction = createEngine(KeywordExtraction.class);
		AnalysisEngine correctAnswerCategoryAndHypernymDetection = createEngine(CategoryAndHypernymDetection.class);
		AnalysisEngine categoryRanking = createEngine(CategoryRanking.class);
		AnalysisEngine candidateExtraction = createEngine(CandidateExtraction.class);
		AnalysisEngine synonymResolution = createEngine(SynonymResolution.class);
		AnalysisEngine candidateAnswerCategoryAndHypernymDetection = createEngine(CategoryAndHypernymDetection.class);
		AnalysisEngine similarityDetection = createEngine(SimilarityDetection.class);
		AnalysisEngine candidateSelection = createEngine(CandidateSelection.class);
		
		SimplePipeline.runPipeline(
				questionParser,
				keywordExtraction,
				correctAnswerCategoryAndHypernymDetection,
				categoryRanking,
				candidateExtraction,
				synonymResolution,
				candidateAnswerCategoryAndHypernymDetection,
				similarityDetection,
				candidateSelection);
	}
	
}
