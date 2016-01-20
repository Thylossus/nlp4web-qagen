package tag.cloud.enrichment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.NonEmptyIntegerList;
import org.apache.uima.jcas.cas.NonEmptyStringList;

import types.CandidateAnswer;
import types.CorrectAnswer;

public class CategoryDetection extends JCasAnnotator_ImplBase {

	public static final String PARAM_SEARCH_TYPE = "SearchType";

	@ConfigurationParameter(name = PARAM_SEARCH_TYPE, mandatory = true)
	private String searchType;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		ExecutorService service = Executors.newFixedThreadPool(4);

		if (searchType == "correctAnswer") {
			List<Future<Result>> tasks = new ArrayList<Future<Result>>();

			List<CorrectAnswer> answers = (List<CorrectAnswer>) JCasUtil.select(jcas, CorrectAnswer.class);
			for (int i = 0; i < answers.size(); i++) {
				CorrectAnswer answer = answers.get(i);
				String searchTerm = answer.getCoveredText();

				List<String> keywordArrayList = new ArrayList<String>();
				NonEmptyStringList keywordList = answer.getKeywords();
				while (keywordList instanceof NonEmptyStringList) {
					keywordArrayList.add(keywordList.getHead());
					keywordList = (NonEmptyStringList) keywordList.getTail();
				}
				String[] keywords = (String[]) keywordArrayList.toArray();

				tasks.add(service.submit(new CategorySearch(searchTerm, keywords)));
			}

			for (int i = 0; i < answers.size(); i++) {
				CorrectAnswer answer = answers.get(i);
				Future<Result> task = tasks.get(i);

				try {
					Result searchResult = task.get();
					List<Integer> categoryList = new ArrayList<Integer>();
					categoryList.addAll(searchResult.getCategoryIds());

					NonEmptyIntegerList categoryIds = new NonEmptyIntegerList(jcas);
					for (int id : categoryList) {
						categoryIds.setHead(id);
						categoryIds.setTail(categoryIds);
					}

					List<Integer> articleList = new ArrayList<Integer>();
					articleList.addAll(searchResult.getArticleIds());

					NonEmptyIntegerList articleIds = new NonEmptyIntegerList(jcas);
					for (int id : articleList) {
						articleIds.setHead(id);
						articleIds.setTail(articleIds);
					}
					answer.setCategories(categoryIds);
					answer.setArticles(articleIds);

				} catch (final InterruptedException ex) {
					ex.printStackTrace();
				} catch (final ExecutionException ex) {
					ex.printStackTrace();
				}
			}

			service.shutdownNow();
		} else if (searchType == "candidateAnswer") {
			List<Future<Result>> tasks = new ArrayList<Future<Result>>();

			List<CandidateAnswer> answers = (List<CandidateAnswer>) JCasUtil.select(jcas, CandidateAnswer.class);
			for (int i = 0; i < answers.size(); i++) {
				CandidateAnswer answer = answers.get(i);
				String searchTerm = answer.getCoveredText();
				tasks.add(service.submit(new CategorySearch(searchTerm)));
			}

			for (int i = 0; i < answers.size(); i++) {
				CandidateAnswer answer = answers.get(i);
				Future<Result> task = tasks.get(i);

				try {
					Result searchResult = task.get();
					List<Integer> categoryList = new ArrayList<Integer>();
					categoryList.addAll(searchResult.getCategoryIds());

					NonEmptyIntegerList categoryIds = new NonEmptyIntegerList(jcas);
					for (int id : categoryList) {
						categoryIds.setHead(id);
						categoryIds.setTail(categoryIds);
					}

					List<Integer> articleList = new ArrayList<Integer>();
					articleList.addAll(searchResult.getArticleIds());

					NonEmptyIntegerList articleIds = new NonEmptyIntegerList(jcas);
					for (int id : articleList) {
						articleIds.setHead(id);
						articleIds.setTail(articleIds);
					}
					answer.setCategories(categoryIds);
					answer.setArticles(articleIds);

				} catch (final InterruptedException ex) {
					ex.printStackTrace();
				} catch (final ExecutionException ex) {
					ex.printStackTrace();
				}
			}

			service.shutdownNow();
		}
	}
}
