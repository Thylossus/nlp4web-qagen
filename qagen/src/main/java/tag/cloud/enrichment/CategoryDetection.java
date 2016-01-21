package tag.cloud.enrichment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import types.CandidateAnswer;
import types.CorrectAnswer;
import util.UimaListHandler;

public class CategoryDetection extends JCasAnnotator_ImplBase {

	public static final String PARAM_SEARCH_TYPE = "SearchType";

	@ConfigurationParameter(name = PARAM_SEARCH_TYPE, mandatory = true)
	private String searchType;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		ExecutorService service = Executors.newFixedThreadPool(4);

		if (searchType == "correctAnswer") {
			List<Future<Result>> tasks = new ArrayList<Future<Result>>();

			for (CorrectAnswer answer : JCasUtil.select(jcas, CorrectAnswer.class)) {
				String searchTerm = answer.getCoveredText();
				this.getContext().getLogger().log(Level.INFO, "Search Term: " + searchTerm);
				List<String> keywordList = UimaListHandler.listToJavaStringList(answer.getKeywords());
				String[] keywords = keywordList.toArray(new String[0]);
				this.getContext().getLogger().log(Level.INFO, "Keywords: " + Arrays.toString(keywords));
				tasks.add(service.submit(new CategorySearch(searchTerm, keywords)));
			}

			Iterator<Future<Result>> it = tasks.iterator();
			for (CorrectAnswer answer : JCasUtil.select(jcas, CorrectAnswer.class)) {
				Future<Result> task = null;
				if (it.hasNext()) {
					task = it.next();

					try {
						Result searchResult = task.get();
						Set<Integer> categorySet = searchResult.getCategoryIds();
						this.getContext().getLogger().log(Level.INFO, "Category Set: " + categorySet.size() + "\n");
						answer.setCategories(
								UimaListHandler.integerCollectionToList(jcas, categorySet));
						Set<Integer> answerSet = searchResult.getArticleIds();
						this.getContext().getLogger().log(Level.INFO, "Answer Set: " + answerSet.size() + "\n");
						answer.setArticles(UimaListHandler.integerCollectionToList(jcas, answerSet));
					} catch (final InterruptedException ex) {
						ex.printStackTrace();
					} catch (final ExecutionException ex) {
						ex.printStackTrace();
					}
				}
			}

			service.shutdownNow();
		} else if (searchType == "candidateAnswer") {
			List<Future<Result>> tasks = new ArrayList<Future<Result>>();

			for (CandidateAnswer answer : JCasUtil.select(jcas, CandidateAnswer.class)) {
				String searchTerm = answer.getCoveredText();
				this.getContext().getLogger().log(Level.INFO, "Search Term: " + searchTerm);
				tasks.add(service.submit(new CategorySearch(searchTerm)));
			}

			Iterator<Future<Result>> it = tasks.iterator();
			for (CandidateAnswer answer : JCasUtil.select(jcas, CandidateAnswer.class)) {
				Future<Result> task = null;
				if (it.hasNext()) {
					task = it.next();
					
					try {
						Result searchResult = task.get();
						Set<Integer> categorySet = searchResult.getCategoryIds();
						this.getContext().getLogger().log(Level.INFO, "Category Set: " + Arrays.toString(categorySet.toArray()) + "\n");
						answer.setCategories(
								UimaListHandler.integerCollectionToList(jcas, categorySet));
						Set<Integer> answerSet = searchResult.getArticleIds();
						this.getContext().getLogger().log(Level.INFO, "Answer Set: " + Arrays.toString(answerSet.toArray()) + "\n");
						answer.setArticles(UimaListHandler.integerCollectionToList(jcas, answerSet));
					} catch (final InterruptedException ex) {
						ex.printStackTrace();
					} catch (final ExecutionException ex) {
						ex.printStackTrace();
					}
				}
			}

			service.shutdownNow();
		}
	}
}
