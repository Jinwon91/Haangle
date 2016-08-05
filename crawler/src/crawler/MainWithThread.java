package crawler;

import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;

public class MainWithThread {
	public static void main(String[] args) {
		 String[] href = {"http://www.chosun.com", "http://www.hani.co.kr", "http://www.yonhapnews.co.kr", "http://www.donga.com", "http://www.ytn.co.kr"};
		//String[] href = {"http://www.ccdailynews.com", "http://www.kyeonggi.com/", "http://www.kyongbuk.co.kr/", "http://www.zdnet.co.kr/", "http://www.hankyung.com/"};
		JavaWebCrawlerWithThread[] crawler = new JavaWebCrawlerWithThread[href.length];
		Thread[] threads = new Thread[href.length];
		try {
			int i;
			for(i=0; i<href.length; i++){
				crawler[i] = new JavaWebCrawlerWithThread(href[i]);
				threads[i] = new Thread(crawler[i]);
				threads[i].start();
			}
		} catch(Exception e) {
			System.out.println("!!!!");
		} 
	}
}
