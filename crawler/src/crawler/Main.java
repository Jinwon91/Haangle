package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.htmlparser.util.ParserException;

public class Main {
	public static void main(String[] args) throws ParserException, IOException {
		Scanner scan = new Scanner(System.in);
		String href;
		
		System.out.println("주소 입력 : ");
		href = "http://www.chosun.com";
		
		try {
			JavaWebCrawler crawler = new JavaWebCrawler();
			crawler.crawler(href);
		} catch(ClientProtocolException cpe) {
			System.out.println("!!!!");
		} 
	}
}
