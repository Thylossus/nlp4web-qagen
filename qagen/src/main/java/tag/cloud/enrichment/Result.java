package tag.cloud.enrichment;

import java.util.Set;

public class Result {
	private Set<Integer> categoryIds;
	private Set<Integer> articleIds;
	
	public Result(Set<Integer> categoryIds, Set<Integer> articleIds){
		this.categoryIds = categoryIds;
		this.articleIds = articleIds;
	}
	
	public Set<Integer> getCategoryIds(){
		return categoryIds;
	}
	
	public Set<Integer> getArticleIds(){
		return articleIds;
	}
}
