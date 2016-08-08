package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;

import org.apache.ibatis.session.SqlSession;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class JavaWebCrawlerWithThread implements Runnable {

	static ArrayList<String> list = new ArrayList<>();
	SqlSession ss = DBService.getFactory().openSession(true);
	StringTokenizer st;
	Queue<String> queue = new LinkedList<String>();
	private String url;
	
	public JavaWebCrawlerWithThread() {}
	public JavaWebCrawlerWithThread(String url) {
		this.url = url;
	}
	
	public void crawler(String startURL) throws IOException, ParserException {
		int i;

		// 큐랑 완료된거에 시작 주소 넣어놓고 시작
		queue.add(startURL);

		Document doc = Jsoup.connect(startURL).get();
		System.out.println("---------------------------------------------------------");

		Elements links;
		String bodyContent = doc.select("body").text();
		while (!queue.isEmpty()) { // 큐가 비어있으면 종료
			try {
				List<Word> wordList = new ArrayList<>();
				String nextURL = queue.remove();
	
				System.out.println("This URL : " + nextURL);
				if (bodyContent != null || bodyContent.trim().equals("")) {
					doc = Jsoup.connect(nextURL).get();
					String html = doc.html();
					Document doc2 = Jsoup.parse(html);
					
					Elements elements = doc2.select("body");
					Elements del_element = elements.select("a, ul, span, h2, h3, h4, h5, h6, h7, dl, table");
					del_element.empty();
					String res = elements.text();
					System.out.println(res);
					// url에 해당하는 내용 삽입
					insertContent(res, nextURL, doc.select("title").text());

					// body 태그 단어 삽입
					int positionContent = 0;
					st = new StringTokenizer(res, "?.!"); // 문장

					while (st.hasMoreTokens()) {
						insertWord(st.nextToken(), positionContent + "", wordList);
						positionContent++;
					}
	
					wordListInsert(wordList);
	
					links = doc.select("a");
					for (Element link : links) {
						if (link.attr("abs:href").startsWith("http") && link.attr("abs:href").contains(url.substring(11))) {
							boolean flag = false;
							for (String str : list) {
								if (link.attr("abs:href").equals(str)) {
									flag = true;
									break;
								}
							}
							if (!flag) {
								list.add(link.attr("abs:href"));
								queue.add(link.attr("abs:href"));
							}
	
						}
					}
				}
			}catch(Exception e){}
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
	public void insertWord(String splitStr, String position, List<Word> wordList) {
		/*System.out.println(position);*/
		Komoran komoran = new Komoran("lib/models-light");
		List<List<Pair<String, String>>> result2 = komoran.analyze(splitStr);
		try {
			for (List<Pair<String, String>> eojeolResult : result2) {
				for (Pair<String, String> wordMorph : eojeolResult) {
					if (wordMorph.getSecond().equals("NNG")) {
						// || wordMorph.getSecond().equals("VX") 동사 제거
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

	public void wordListInsert(List<Word> wordList) {
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
		int up20 = wordList.size()/5;
		int count = 0;
		while (itr.hasNext()) {
			count++;
			if (count >= up20+30)
				break;
			if (count > up20)
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

	@Override
	public void run() {
		try {
			crawler(url);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
