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
			JavaWebCrawlerWithThread crawler = new JavaWebCrawlerWithThread(href);
			new Thread(crawler).start();
		} catch(Exception cpe) {
			System.out.println("!!!!");
		} 
	}
}
