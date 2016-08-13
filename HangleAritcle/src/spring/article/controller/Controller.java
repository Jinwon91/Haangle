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
				StringTokenizer st = new StringTokenizer(contentVO.getContent(), "?.!");
				idx = 0;
				count = 0;
				String result="", temp;
				while(st.hasMoreTokens()){
					temp = st.nextToken();
						result += temp;  
						count++;
						if(count == 2)
							break;
					idx++;
				}
				System.out.println(result);
			/*	System.out.print(contentVO.getPosition()+" : ");
				System.out.println(result);*/
				result = result.replaceAll(search, "<b>" + search + "</b>");
				
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
