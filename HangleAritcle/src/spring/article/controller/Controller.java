package spring.article.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;
import spring.article.db.ContentVO;
import spring.article.db.Dao;
import spring.article.db.Page;
import spring.article.db.SentenceVO;

@org.springframework.stereotype.Controller
public class Controller {
	private Dao dao;
	private Page page;
	public Dao getDao() { return dao; }
	public void setDao(Dao dao) { this.dao = dao; }
	public Page getPage() { return page; }
	public void setPage(Page page) { this.page = page; }
	
	@RequestMapping("/search.do")
	public ModelAndView search(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("searchView");
		String search = request.getParameter("search");
		
		Komoran komoran = new Komoran("/models-light");
		List<String> sent = new ArrayList<>();
		String sentence = "(";
		List<List<Pair<String, String>>> result2 = komoran.analyze(search);
		try {
			for (List<Pair<String, String>> eojeolResult : result2) {
				for (Pair<String, String> wordMorph : eojeolResult) {
					if (wordMorph.getSecond().equals("NNG")|| wordMorph.getSecond().equals("NNP")) {
						/*sent.add(wordMorph.getFirst());*/
						sentence += "'";
						sentence += wordMorph.getFirst();
						sentence += "'";
						sentence += ", ";
						sent.add(wordMorph.getFirst());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sentence = sentence.substring(0,sentence.length()-2);
		sentence += ")";

		int totalRecord = dao.getTotalCount(sentence);
		
		System.out.println(totalRecord);
		
		if(totalRecord <=0){
			mv.addObject("list",null);
			return mv;		
		}
		
		page.setTotalRecord(totalRecord);
		page.setTotalPage();		
		String cPage = request.getParameter("cPage");
		if(cPage != null)
			page.setNowPage(Integer.parseInt(cPage));		
		page.setBegin((page.getNowPage()-1)*page.getNumPerPage()+1);
		page.setEnd(page.getBegin()+page.getPagePerBlock()-1);			
		page.setBeginPage((page.getNowPage()-1)/page.getPagePerBlock()*page.getPagePerBlock()+1);
		page.setEndPage(page.getBeginPage()+page.getPagePerBlock()-1);
		
		if(page.getEndPage() > page.getTotalPage())
			page.setEndPage(page.getTotalPage());
		
		Map<String, Object> map = new HashMap<>();
		map.put("begin", page.getBegin());
		map.put("end", page.getEnd());
		map.put("keyword", sentence);	
		List<ContentVO> list = dao.searchContent(map);	
		int idx;
		int count;
		for (ContentVO contentVO : list) {
			if(contentVO.getContent() != null){
				StringTokenizer st2 = new StringTokenizer(contentVO.getPosition(), "/");
//				System.out.println(contentVO.getPosition());
				int[] position = new int[st2.countTokens()];
				int c = 0;
				while(st2.hasMoreTokens()){
					position[c] = Integer.parseInt(st2.nextToken());
//					System.out.print(position[c] + " ");
					if(st2.countTokens() == c)
						c = 0;
					c++;
				}
				int a = 0;
				for(int k = 0;k<position.length;k++){
					for(int j = 0;j<=k;j++){
						if(position[j]>position[k]){
							a = position[j];
							position[j] = position[k];
							position[k] = a;
						}
					}
				}
//				for(int k = 0; k<position.length;k++ )
//					System.out.print(position[k] + " ");
				
//				System.out.println();
				StringTokenizer st = new StringTokenizer(contentVO.getContent(), "?.!");
				idx = 0;
				String result="", temp;
				
				for(int k = 0; k<position.length;k++ ){
					count = 0;
					while(st.hasMoreTokens()){
						temp = st.nextToken();
						if(position[k] == count){
							result += (temp + ".......");
//							System.out.print(position[k]+"1");
//							System.out.print(count+" ");
							count=0;
							break;
						}
						count++;
					}
				}

//				sent.add(wordMorph.getFirst());
				for (String i : sent) {
//					System.out.println(i);
					result = result.replaceAll(i, "<b>" + i + "</b>");
				}
//				result = result.replaceAll(search, "<b>" + search + "</b>");
				
				contentVO.setContent(result);
				
				idx = 0;
				count = 0;
				
			} else {
				contentVO.setContent("");
			}
		}
		
	/*	public void insertWord(String splitStr, String position, List<Word> wordList) {*/
			/*System.out.println(position);*/

		
		
		mv.addObject("list", list);
		mv.addObject("search", search);
		mv.addObject("page", page);

		return mv;
	}
}
