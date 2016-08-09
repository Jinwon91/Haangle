package spring.article.db;

public class ContentVO {
	private String content_idx, url, content, title, regdate, hit, position, pagerank;

	public String getPagerank() {
		return pagerank;
	}
	public void setPagerank(String pagerank) {
		this.pagerank = pagerank;
	}
	public String getContent_idx() {
		return content_idx;
	}
	public void setContent_idx(String content_idx) {
		this.content_idx = content_idx;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getHit() {
		return hit;
	}
	public void setHit(String hit) {
		this.hit = hit;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
