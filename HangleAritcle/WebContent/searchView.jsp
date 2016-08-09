<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	
</script>
<style type="text/css"></style>
<style type="text/css">
	table {
		border-spacing: 2px;
		padding:0;
		margin-left : auto; margin-right : auto;
		font-size:13px;
		text-align:center;
	}
	li {
		list-style: none;
	}
	a {
		text-decoration: none;
	}
	li.link {
		color: green;
	}
	ul {
		border: 1px solid black;
	}
	img {
		height: 30px;
		width: 85%;
	}
	
</style>
<title>Insert title here</title>
</head>
<body>
	<c:if test="${!empty list}">
		<c:forEach items="${list }" var="k">
			<ul>
				<li><a href="${k.url}">${k.title }</a></li>
				<li class="link">${k.url }</li>
				<li>${k.content}</li>
			</ul>
		</c:forEach>	
	</c:if>
	<c:if test="${empty list}">
		<ul>
			<li>자료가 없습니다</li>
		</ul>
	</c:if>
	<div id="pageIndex">
		<table>
			<tr>
				<c:if test="${page.beginPage > page.pagePerBlock }">
				<td><a href="search.do?cPage=${page.beginPage-page.pagePerBlock}&search=${search}"><span> < </span></a></td>
				</c:if>
				<td>
					<a href="search.do?cPage=${page.beginPage-page.pagePerBlock}&search=${search}">
						<img src="images/paging_prev.png" style="width:25px;"/>
					</a>
				</td>
				<c:forEach var="a" begin="${page.beginPage}" end="${page.endPage }" step="1">
					<td>
						<a href = "search.do?cPage=${a}&search=${search}">
							<span>
								<c:if test="${a != page.nowPage}"><img src="images/paging_disabled.png" style="width:15px;"/></c:if>
								<c:if test="${a == page.nowPage}"><img src="images/paging_enabled.png" style="width:15px;"/></c:if>
							</span>
						</a>
					</td>
				</c:forEach>
				<td>
					<a href="search.do?cPage=${page.beginPage+page.pagePerBlock}&search=${search}">
						<span>
							<img src="images/paging_next.png" style="width:60px;"/>
						</span>
					</a>
				</td>
				<c:if test="${page.endPage != page.totalPage }">
					<td><a href="search.do?cPage=${page.beginPage+page.pagePerBlock}&search=${search}"> > </a></td>
				</c:if>
			</tr>
			<tr>
				<c:if test="${page.beginPage > page.pagePerBlock }">
					<td><a href="search.do?cPage=${page.beginPage-page.pagePerBlock}&search=${search}">이전</a></td>
				</c:if>
				<td> </td>
				
				<c:forEach var="a" begin="${page.beginPage}" end="${page.endPage }" step="1">
					<td style = "text-align: center">
						<a href = "search.do?cPage=${a}&search=${search}">${a}</a>
					</td>
				</c:forEach>
				<td> </td>
				<c:if test="${page.endPage != page.totalPage }">
					<td><a href="search.do?cPage=${page.beginPage+page.pagePerBlock}&search=${search}">다음</a></td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>