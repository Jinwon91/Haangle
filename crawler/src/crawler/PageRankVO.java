package crawler;

public class PageRankVO {
	private String url;
	private Integer pageRank=0;
	
	public PageRankVO(){}
	public PageRankVO(String url) {
		super();
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getPageRank() {
		return pageRank;
	}
	public void setPageRank(Integer pageRank) {
		this.pageRank = pageRank;
	}
	
	public void rankPlus(){
		pageRank++;
	}
}
