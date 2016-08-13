package spring.article.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

public class Dao {
	private SqlSessionTemplate template;

	public SqlSessionTemplate getTemplate() { return template; }
	public void setTemplate(SqlSessionTemplate template) { this.template = template; }

	
	
	// 해당 범위의 데이터 가져오기
/*	public List<ContentVO> searchContent(Map<String, Object> map){
		List<WordVO> wordList = template.selectList("searchKeyword", map);
		if(wordList.size()!=0){
			List<ContentVO> list = new ArrayList<>();
			for(int i=0; i<wordList.size(); i++)
				list.add(template.selectOne("searchContent", wordList.get(i).getContent_idx()));
			
			for(int i=0; i<list.size(); i++)
				list.get(i).setPosition(wordList.get(i).getPosition());
			
			for(int i=0; i<list.size(); i++){
				System.out.println("content : " + list.get(i).getContent());
				System.out.println("contentVO : " + list.get(i).getPosition());
				System.out.println("wordVO : " + wordList.get(i).getPosition());
			}

			return list;
		}
		return null;
	}*/
	
	// 해당 범위의 데이터 가져오기
	public List<ContentVO> searchContent(Map<String, Object> map){
		List<SentenceVO> sentence = template.selectList("searchKeyword", map);
		if(sentence.size()!=0){
			// List<ContentVO> list = template.selectList("searchContent", wordList);
			List<ContentVO> list = new ArrayList<>();
			for(int i=0; i<sentence.size(); i++)
				list.add(template.selectOne("searchContent", sentence.get(i).getContent_idx()));
			/*for(int i=0; i<list.size(); i++)
				list.get(i).setPosition(wordList.get(i).getPosition());*/
			
			for(int i=0; i<list.size(); i++){
				/*				System.out.println("content : " + list.get(i).getContent());
				System.out.println("contentVO : " + list.get(i).getPosition());
				System.out.println("wordVO : " + wordList.get(i).getPosition());*/
			}
			
			return list;
		}
		return null;
	}//문제
	// 총 레코드 수 가져오기
	public int getTotalCount(String keyword){
		System.out.println(keyword);
		
		int wordList = template.selectOne("searchAllKeyWord", keyword);
		if(wordList != 0)
			return wordList;
		return 0;
	}
	
	
}