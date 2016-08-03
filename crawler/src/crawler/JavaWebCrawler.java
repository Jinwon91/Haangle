package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	Queue<String> queue = new LinkedList<>();
	int rear, front;

	public void crawler(String href) throws IOException, ParserException {
		try {
			rear = 0;
			front = 0;
			
			HttpPost http = new HttpPost(href);

			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpResponse response = httpClient.execute(http);

			HttpEntity entity = response.getEntity();

			ContentType contentType = ContentType.getOrDefault(entity);
			Charset charset = contentType.getCharset();

			BufferedReader br = null;

			if (charset != null) {
				br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
			} else {
				charset = Charset.defaultCharset();
				br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
			}

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			// Document doc = Jsoup.parse(sb.toString());
			Document doc = Jsoup.connect(href).get();

			System.out.println("---------------------------------------------------------");
			System.out.println("charset : " + charset);
			String content = doc.select("body:not(ul a)").text();
			String title = doc.select("title").text();
			int position = 0;

			insertContent(content, href, title);

			StringTokenizer st = new StringTokenizer(content, "?.!");
			while (st.hasMoreTokens()) {
				insertWord(st.nextToken(), position + "");
				position++;
			}
			
			System.out.println("---------------------------------------------------------");

			Elements eles = doc.select("body");
			boolean flag = false;
			if (eles.get(0).text() != null || eles.get(0).text().trim().equals("")) {
				eles = doc.select("a");
				for (Element e : eles) {
					if (e.attr("href").startsWith("http")) {
						for (String str : list) {
							if (e.attr("href").equals(str)) {
								flag = true;
								break;
							}
						}
						if (!flag) {
							System.out.println("HREF : " + e.attr("href"));
							list.add(e.attr("href"));
							JavaWebCrawler crawler = new JavaWebCrawler();
							crawler.crawler(e.attr("href"));
						}
					}
				}
			}
		} catch (Exception e) {
			return;
		}
	}
	
	//접근한 url과 해당 페이지 내용 insert
	public void insertContent(String content, String url, String title) {
		Map<String, String> map = new HashMap<>();
		map.put("url", url);
		map.put("content", content);
		map.put("title", title);
		ss.insert("InsertContent", map);
	}

	//해당 페이지 내의 내용 형태소 분석 후 단어별 저장 (명사 ,부정동사)
	public void insertWord(String splitStr, String position) {
		Komoran komoran = new Komoran("C:\\Users\\ChoiJinwoong\\Downloads\\models-light");
		List<List<Pair<String, String>>> result2 = komoran.analyze(splitStr);
		try {
			for (List<Pair<String, String>> eojeolResult : result2) {
				for (Pair<String, String> wordMorph : eojeolResult) {
					if (wordMorph.getSecond().equals("NNG") || wordMorph.getSecond().equals("VX")) {
						Map<String, String> map = new HashMap<>();
						wordMorph.getFirst();
						map.put("word", wordMorph.getFirst());
						map.put("word_type", wordMorph.getSecond());
						map.put("position", position);
						ss.insert("InsertWord", map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
