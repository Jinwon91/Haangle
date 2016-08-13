package spring.article.db;

public class SentenceVO {
	private String content_idx, content, count, rank;
	
	
	public SentenceVO(String content_idx, String content, String count, String rank) {
		super();
		this.content_idx = content_idx;
		this.content = content;
		this.count = count;
		this.rank = rank;
	}

	public String getContent_idx() {
		return content_idx;
	}

	public void setContent_idx(String content_idx) {
		this.content_idx = content_idx;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public SentenceVO() {	}
	

}
