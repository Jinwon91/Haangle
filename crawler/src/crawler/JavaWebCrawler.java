package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.ibatis.session.SqlSession;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class JavaWebCrawler {

	static ArrayList<String> list = new ArrayList<>();
	SqlSession ss = DBService.getFactory().openSession(true);
	List<Word> wordList = new ArrayList<>();
	StringTokenizer st;

	static Queue<String> queue = new LinkedList<String>();
	static LinkedHashSet<String> marked = new LinkedHashSet<String>();
	private static final int NUMBER_OF_LINKS = 1000;

	public void crawler(String startURL) throws IOException, ParserException {
		try {
			int i;
			
			// 큐랑 완료된거에 시작 주소 넣어놓고 시작
			queue.add(startURL);
			marked.add(startURL);

			Document doc = Jsoup.connect(startURL).get();
			System.out.println("---------------------------------------------------------");

			Elements links;
			String bodyContent = doc.select("body").text();
			while (!queue.isEmpty()) { // 큐가 비어있으면 종료
				boolean flag = false;
				String nextURL = queue.remove();

				System.out.println("This URL : "+nextURL);
				if (bodyContent != null || bodyContent.trim().equals("")) {
					doc = Jsoup.connect(nextURL).get();
					// url에 해당하는 내용 삽입
					insertContent(doc.select("body:not(ul a)").text(), nextURL, doc.select("title").text());
					String h1 = doc.select("h1").text(); // h1 태그 값

					// h1 태그 단어 삽입
					int positionH1 = 0;
					st = new StringTokenizer(h1, "?.!");
					while (st.hasMoreTokens()) {
						insertWord(st.nextToken(), positionH1 + "");
						positionH1++;
					}

					// body 태그 단어 삽입
					int positionContent = 0;
					st = new StringTokenizer(doc.select("body:not(ul a)").text(), "?.!"); // 문장 단위로 나눔
					while (st.hasMoreTokens()) {
						insertWord(st.nextToken(), positionContent + "");
						positionContent++;
					}
			
					wordListInsert();

					links = doc.select("a");
					for (Element link : links) {
						if (link.attr("abs:href").startsWith("http") && link.attr("abs:href").contains("chosun.com")) {
							for (String str : queue) {
								if (link.attr("abs:href").equals(str)) {
									flag = true;
									break;
								}
							}
							if(!flag){
								System.out.println("HREF : " + link.attr("href"));
								queue.add(link.attr("abs:href"));
							}
							
						}
					}
				}
			}					
		} catch (Exception e) {
			e.printStackTrace();
			return;
			
		}
	}

	// 접근한 url과 해당 페이지 내용 insert
	public void insertContent(String content, String url, String title) {
		Map<String, String> map = new HashMap<>();
		map.put("url", url);
		map.put("content", content);
		map.put("title", title);
		ss.insert("InsertContent", map);
	}

	// 해당 페이지 내의 내용 형태소 분석 후 단어별 저장 (명사 ,부정동사)
	public void insertWord(String splitStr, String position) {
		Komoran komoran = new Komoran("lib/models-light");
		List<List<Pair<String, String>>> result2 = komoran.analyze(splitStr);
		try {
			for (List<Pair<String, String>> eojeolResult : result2) {
				for (Pair<String, String> wordMorph : eojeolResult) {
					if (wordMorph.getSecond().equals("NNG") || wordMorph.getSecond().equals("VX")) {
						int i;
						boolean flag = true;
						for (i = 0; i < wordList.size(); i++) {
							if (wordMorph.getFirst().equals(wordList.get(i).getWord())) {
								flag = false;
								wordList.get(i).setHit(wordList.get(i).getHit() + 1);
							}
						}
						if (flag) {
							wordList.add(new Word(wordMorph.getFirst(), Integer.parseInt(position), wordMorph.getSecond()));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void wordListInsert() {
		// WordList based by hit Sorting
		Collections.sort(wordList, new Comparator<Word>() {
			@Override
			public int compare(Word o1, Word o2) {
				if (o1.getHit() > o2.getHit())
					return -1;
				else if (o1.getHit() < o2.getHit())
					return 1;
				else
					return 0;
			}
		});

		Iterator<Word> itr = wordList.iterator();
		int count = 0;
		while (itr.hasNext()) {
			count++;
			if (count >= 17)
				break;
			if (count > 2)
				insert(itr.next());
		}
	}

	// Word 삽입
	public void insert(Word word) {
		Map<String, String> map = new HashMap<>();
		map.put("word", word.getWord());
		map.put("word_type", word.getWord_type());
		map.put("position", word.getPosition() + "");
		ss.insert("InsertWord", map);
	}
}

class Word {
	private int position;
	private String word;
	private String word_type;
	private int hit;

	public Word() {
	}

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
