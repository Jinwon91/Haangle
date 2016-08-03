package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
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

	static Queue<String> queue = new LinkedList<String>();
	static LinkedHashSet<String> marked = new LinkedHashSet<String>();
	private static final int NUMBER_OF_LINKS = 1000;

	public void crawler(String startURL) throws IOException, ParserException {
		try {
//
//			HttpPost http = new HttpPost(startURL);
//
//			HttpClient httpClient = HttpClientBuilder.create().build();
//
//			HttpResponse response = httpClient.execute(http);
//
//			HttpEntity entity = response.getEntity();
//
//			ContentType contentType = ContentType.getOrDefault(entity);
//			Charset charset = contentType.getCharset();
//
//			BufferedReader br = null;
//
//			if (charset != null) {
//				br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
//			} else {
//				charset = Charset.defaultCharset();
//				br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
//			}
//
//			StringBuffer sb = new StringBuffer();
//
//			String line = "";
//			while ((line = br.readLine()) != null) {
//				sb.append(line + "\n");
//			}

			// 큐랑 완료된거에 시작 주소 넣어놓고 시작
			queue.add(startURL);
			marked.add(startURL);

			// Document doc = Jsoup.parse(sb.toString());
			Document doc = Jsoup.connect(startURL).get();

			System.out.println("---------------------------------------------------------");
//			System.out.println("charset : " + charset);

			System.out.println("---------------------------------------------------------");

			Elements links;

			String bodyContent = doc.select("body").text();
			boolean flag = false;
			while (!queue.isEmpty()) { // 큐가 비어있으면 종료
				String nextURL = queue.remove();
				//
				System.out.println("This URL : "+nextURL);
				if (bodyContent != null || bodyContent.trim().equals("")) {
					doc = Jsoup.connect(nextURL).get();
					//
					insertContent(doc.select("body:not(ul a)").text(), nextURL, doc.select("title").text());

					StringTokenizer st = new StringTokenizer(doc.select("body:not(ul a)").text(), "?.!");

					int position = 0;
					while (st.hasMoreTokens()) {
						insertWord(st.nextToken(), position + "");
						position++;
					}
					//
					links = doc.select("a");
					for (Element link : links) {
						if (link.attr("abs:href").startsWith("http") && link.attr("abs:href").contains("chosun.com")) {
							for (String str : queue) {
								if (link.attr("abs:href").equals(str)) {
									flag = true;
									break;

									// pageRank할 때 여기다가 넣어야할듯
								}
							}
							if (!flag) {
								System.out.println("HREF : " + link.attr("href"));

								queue.add(link.attr("abs:href"));
							}
						}
					}
				} // if end
			} // while end

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
