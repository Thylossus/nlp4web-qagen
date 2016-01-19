package pipeline;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tag.cloud.enrichment.CategorySearch;
import tag.cloud.enrichment.Result;

public class Experiments {
	public static void main(String[] args) {
		String searchterm = "Angela Merkel";
		String[] keywords = { "chancellor", "germany", "woman" };
		
		ExecutorService service;
        Future<Result>  task;

        service = Executors.newFixedThreadPool(1);        
        task    = service.submit(new CategorySearch(searchterm, keywords));

        try {
           Result searchResult = task.get();
           Set<Integer> categoryIds = searchResult.getCategoryIds();
           Set<Integer> articleIds = searchResult.getArticleIds();
           
           System.out.println("Category IDs:");
           for (int c : categoryIds){
        	   System.out.print(c + ", ");
           }
           System.out.println("\n\nArticle IDs:");
           for (int a : articleIds){
        	   System.out.print(a + ", ");
           }
        } catch(final InterruptedException ex) {
            ex.printStackTrace();
        } catch(final ExecutionException ex) {
            ex.printStackTrace();
        }

        service.shutdownNow();
	}
}
