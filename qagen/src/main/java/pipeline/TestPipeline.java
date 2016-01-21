package pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import consumer.DebuggingOutput;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import input.OpenTriviaQAParser;
import question.processing.KeywordExtraction;

public class TestPipeline {

	public static void main(String[] args) throws UIMAException, IOException {
		CollectionReader questionParser = createReader(OpenTriviaQAParser.class, OpenTriviaQAParser.PARAM_INPUT_FILE, "questions/questions-tobiask.txt");
		
		AnalysisEngine segTagger = createEngine(StanfordSegmenter.class);
		AnalysisEngine posTagger = createEngine(StanfordPosTagger.class);
		AnalysisEngine nerTagger = createEngine(StanfordNamedEntityRecognizer.class);
		AnalysisEngine keywordExtraction = createEngine(KeywordExtraction.class);
		AnalysisEngine output = createEngine(DebuggingOutput.class);
		
		SimplePipeline.runPipeline(
				questionParser,
				segTagger,
				posTagger,
				nerTagger,
				keywordExtraction,
				output
		);
	}

}
