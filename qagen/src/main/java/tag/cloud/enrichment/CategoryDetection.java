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

import config.DBConfig;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
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
						Set<Integer> answerSet = searchResult.getArticleIds();

						if (categorySet.isEmpty() && answerSet.isEmpty()) {
							this.getContext().getLogger().log(Level.WARNING, "Category and article set are empty for correct answer");
						} else {
							this.getContext().getLogger().log(Level.INFO, "Category Set: " + categorySet.size() + "\n");
							answer.setCategories(UimaListHandler.integerCollectionToList(jcas, categorySet));
							this.getContext().getLogger().log(Level.INFO, "Article Set: " + answerSet.size() + "\n");
							answer.setArticles(UimaListHandler.integerCollectionToList(jcas, answerSet));
						}
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
			String[] keywords = null;
			List<String> keywordList;
			Wikipedia wiki;

			try {
				wiki = new Wikipedia(DBConfig.getJwplDbConfig());
			} catch (WikiInitializationException e) {
				throw new AnalysisEngineProcessException(
						"Cannot establish a connection the Wikiepdia database with the JWPL API.", null);
			}

			// Find keywords of correct answer (required for category filtering)
			for (CorrectAnswer ca : JCasUtil.select(jcas, CorrectAnswer.class)) {
				keywordList = UimaListHandler.listToJavaStringList(ca.getKeywords());
				keywords = keywordList.toArray(new String[keywordList.size()]);
			}

			if (keywords == null) {
				throw new AnalysisEngineProcessException("Cannot find a correct answer to retrieve keywords from.",
						null);
			}

			for (CandidateAnswer answer : JCasUtil.select(jcas, CandidateAnswer.class)) {
				try {
					// Look for title of the Wikipedia page as the candidate'
					// name
					Page searchTermPage = wiki.getPage(answer.getWikipediaPageId());
					String searchTerm = searchTermPage.getTitle().getPlainTitle();
					this.getContext().getLogger().log(Level.INFO, "Search Term: " + searchTerm);
					tasks.add(service.submit(new CategorySearch(searchTerm, keywords)));
				} catch (WikiApiException e) {
					throw new AnalysisEngineProcessException("Cannot load Wikipedia article for candidate answer.",
							new Object[] { answer });
				}
			}

			Iterator<Future<Result>> it = tasks.iterator();
			for (CandidateAnswer answer : JCasUtil.select(jcas, CandidateAnswer.class)) {
				Future<Result> task = null;
				if (it.hasNext()) {
					task = it.next();

					try {

						Result searchResult = task.get();

						Set<Integer> categorySet = searchResult.getCategoryIds();
						Set<Integer> answerSet = searchResult.getArticleIds();

						if (categorySet.isEmpty() && answerSet.isEmpty()) {
							this.getContext().getLogger().log(Level.WARNING,
									"Category and article set are empty for candidate answer with wikipedia article id "
											+ answer.getWikipediaPageId());
						} else {
							this.getContext().getLogger().log(Level.INFO,
									"Category Set: " + Arrays.toString(categorySet.toArray()) + "\n");
							answer.setCategories(UimaListHandler.integerCollectionToList(jcas, categorySet));

							this.getContext().getLogger().log(Level.INFO,
									"Article Set: " + Arrays.toString(answerSet.toArray()) + "\n");
							answer.setArticles(UimaListHandler.integerCollectionToList(jcas, answerSet));
						}
					} catch (InterruptedException | ExecutionException | IllegalArgumentException ex) {
						ex.printStackTrace();
					}
				}
			}

			service.shutdownNow();
		}
	}
}
