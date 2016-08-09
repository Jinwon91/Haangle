package spring.article.db;

public class WordVO {
	private String word_idx, word, content_idx, hit, position, word_type;

	public String getWord_idx() {
		return word_idx;
	}
	public void setWord_idx(String word_idx) {
		this.word_idx = word_idx;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getContent_idx() {
		return content_idx;
	}
	public void setContent_idx(String content_idx) {
		this.content_idx = content_idx;
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
	public String getWord_type() {
		return word_type;
	}
	public void setWord_type(String word_type) {
		this.word_type = word_type;
	}
}
