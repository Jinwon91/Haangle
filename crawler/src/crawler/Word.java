package crawler;

class Word {
	private int position;
	private String word;
	private String word_type;
	private int hit;

	public Word() {}
	public Word(String word, int position, String word_type) {
		this.word = word;
		this.word_type = word_type;
		hit = 0;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getWord_type() {
		return word_type;
	}

	public void setWord_type(String word_type) {
		this.word_type = word_type;
	}
}