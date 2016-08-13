package spring.article.db;

public class Position {
	private String position, keyword, content_idx;

	public Position() {
	}

	public Position(String keyword, String content_idx) {
		super();
		this.content_idx = content_idx;
		this.keyword = keyword;
	}

	public String getContent_idx() {
		return content_idx;
	}

	public void setContent_idx(String content_idx) {
		this.content_idx = content_idx;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
