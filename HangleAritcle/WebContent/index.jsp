<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<script type="text/javascript">
	function search_go(f) {
		f.action = "search.do";
		f.submit();
	}
</script>
<style type="text/css">
	*{
		margin: 0; padding: 0;		
	}
	body {
		text-align: center;
		width:60%;
		margin: 0 auto;
	}
	#search{
		padding: 5px;
	}
	#button{
		padding: 10px;
		background-color: #eeeeee;
		font-weight: bold;
		border: none; 
		color: #666666;
	}
	
</style>
<meta charset= "UTF-8">
<title>Insert title here</title>
</head>
<body>
	<img style="width: 300; margin-top: 200px" src="images/haanglepng.png" />
	<form>
		<input id="search" type="text" name="search" size="70%" placeholder="검색어" onkeypress="if(event.keyCode==13) { search_go(this.form);}">
		<br />
		<br />
		<input id="button" type="button" value="Haangle 검색" onclick="search_go(this.form)">
	</form>
</body>
</html>