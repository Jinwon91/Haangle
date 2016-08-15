<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE>
<html>
<head>
<meta charset=UTF-8>
<link rel="shortcut icon" type="image/png" href = "pavi.png" />
<script type="text/javascript">
	function search_go(f) {
		f.action = "search.do";
		f.submit();
	}
	function search() {
		var keyword = $("#search_text").val();
		location.href = "search.do?search=" + keyword;
	}
	
</script>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<style type="text/css"></style>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

b{
	font-weight: bold;
	/* font-style: italic; */
	background-color: #eeeeee;
}

div#bodyContainer {
	margin-left: 140px;
	width: 600px;
	margin-top: 50px;
	position: relative;
}

table {
	border-spacing: 5px;
	padding: 0;
	margin-left: auto;
	margin-right: auto;
	font-size: 13px;
	text-align: center;
}

li {
	list-style: none;
}

a {
	text-decoration: none;
}

li.title {
	font-size: 17px;
}

li.link {
	color: green;
	font-size: 13px;
}

li.content {
	color: #545454;
	font-size: 13px;
}

ul {
	
}

img.pageImg {
	height: 30px;
}

img.mainLogo {
	width: 95px;
	position: fixed;
	left: 20px;
	top: 12px;
}

header#searchNav {
	top: 0;
	z-index: 100;
	position: fixed;
	width: 100%;
	background-color: #f1f1f1;
	height: 50px;
	/* text-align: center; */
}
footer {
	top: 0;
	z-index: 100;
	width: 100%;
	background-color: #f1f1f1;
	height: 50px;
	text-align: center;
}

input#search_text {
	font-size: 15px;
	margin-top: 10px;
	padding: 5px;
	margin-left: 140px;
	border: 1px solid lightgray;
	border-radius: 2px 2px 2px 2px;
}
}
</style>
<title>Haangle 검색 - ${search}</title>
</head>
<body>
	<header id="searchNav">
		<a href="index.jsp"><img class="mainLogo"
			src="images/logo_hanchan.png" alt="메인으로"></a> <input type="text"
			id="search_text" placeholder="Haangle 검색" size="50"
			onkeypress="if(event.keyCode==13) { search();}"
			value = "${search }"
			" />
	</header>

	<div id="bodyContainer">
		<br>
		질의문 분석결과 : 
		<c:forEach items="${keyword }" var="k">
		${k }&nbsp;
			</c:forEach>
			<hr/>
			<br />
		<c:if test="${!empty list}">
			<c:forEach items="${list }" var="k">
				<ul>
					<li class="title"><a href="${k.url}">${k.title }</a></li>
					<li class="link">${k.url }</li>
					<li class="content">${k.content}</li>
				</ul>
				<br>
			</c:forEach>
			<div id="pageIndex">
				<table>
					<tr>
						<c:if test="${page.beginPage > page.pagePerBlock }">
							<td><a
								href="search.do?cPage=${page.beginPage-page.pagePerBlock}&search=${search}"><span>
										< </span></a></td>
						</c:if>
						<td><a
							href="search.do?cPage=${page.beginPage-page.pagePerBlock}&search=${search}">
								<img class="pageImg" src="images/paging_prev.png" />
						</a></td>
						<c:forEach var="a" begin="${page.beginPage}"
							end="${page.endPage }" step="1">
							<td><a href="search.do?cPage=${a}&search=${search}"> <span>
										<c:if test="${a != page.nowPage}">
											<img class="pageImg" src="images/paging_disabled.png" />
										</c:if> <c:if test="${a == page.nowPage}">
											<img class="pageImg" src="images/paging_enabled.png" />
										</c:if>
								</span>
							</a></td>
						</c:forEach>
						<td><a
							href="search.do?cPage=${page.beginPage+page.pagePerBlock}&search=${search}">
								<span> <img class="pageImg" src="images/paging_next.png" />
							</span>
						</a></td>
						<c:if test="${page.endPage != page.totalPage }">
							<td><a
								href="search.do?cPage=${page.beginPage+page.pagePerBlock}&search=${search}">
									> </a></td>
						</c:if>
					</tr>
					<tr>
						<c:if test="${page.beginPage > page.pagePerBlock }">
							<td><a
								href="search.do?cPage=${page.beginPage-page.pagePerBlock}&search=${search}">이전</a></td>
						</c:if>
						<td></td>

						<c:forEach var="a" begin="${page.beginPage}"
							end="${page.endPage }" step="1">
							<td style="text-align: center"><a
								href="search.do?cPage=${a}&search=${search}">${a}</a></td>
						</c:forEach>
						<td></td>
						<c:if test="${page.endPage != page.totalPage }">
							<td><a
								href="search.do?cPage=${page.beginPage+page.pagePerBlock}&search=${search}">다음</a></td>
						</c:if>
					</tr>
				</table>
			</div>
		</c:if>
		<c:if test="${empty list}">
			<ul>
				<li>자료가 없습니다</li>
			</ul>
		</c:if>

	</div>

	<footer id="footer">
		
	</header>
</body>
</html>