<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %> <!--태그라이브라는 애를 불러올 껀데 걔 이름이 c야-->
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>게시판</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

</head>
<body>
<div class="container-fluid">
    <div class="main h-100">
        <!--
            taglib C 는 기본적으로 JSP에서 자바 코드 대신 Marked-up Language 형식으로 변수의 값, 조건문, 반목문 등을
            출력할 수 있도록 만들어주는 태그 라이브러리이다.
        -->
        <!--
            가장 대표적으로 사용할 수 있는 Core 태그는 바로 forEach 태그이다. 바로 반복문이다.

            items 어트7778리뷰트에는 순차적으로 꺼내올 콜렉션 개체를 지정한다.
            var 어트리뷰트는 하나씩 뽑아온 것을 뭐라고 호칭할지를 지정한다. 즉 아래의 forEach 태그는
            for(BoardDTO b : list) {
            }
            와 같은 의미가 된다.

            fmt 는 formatter와 관련된 태그이다. 주로 시간의 포맷을 정할 때 사용이 된다.
        -->

        <div class="row justify-content-center">
            <div class="col-8 text-center">
                <div class="row justify-content-start">
                    <div class="col-3">
                        <a class="btn btn-outline-success" href="/board/write">글 작성하기</a>
                    </div>
                </div>
                <table class="table table-striped">
                    <tr>
                        <th>글 번호</th>
                        <th colspan="3">제목 </th>
                        <th>작성자</th>
                        <th>작성일</th>
                    </tr>
                    <c:forEach items="${list}" var="b">
                        <tr onclick="javascript:location.href='/board/showOne/${b.id}'">
                            <td>${b.id}</td>
                            <td colspan="3">${b.title}</td>
                            <td>${b.nickname}</td>
                            <td><fmt:formatDate value="${b.entryDate}" pattern="yy/MM/dd HH:mm:ss"/> </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="6" class="text-center">
                            <ul class="pagination justify-content-center" >
                                <li class="page-item">
                                    <a class="page-link" href="/board/showAll/1"> << </a>
                                </li>
                                <c:if test="${curPage>5}">
                                    <li class="page-item">
                                    <a href="/board/showAll/${curPage -5}" class="page-link"> < </a>
                                    </li>
                                </c:if>
                                <c:if test="${curPage<=5}">
                                    <li class="page-item disabled">
                                        <a href="/board/showAll/${curPage -5}" class="page-link"> < </a>
                                    </li>
                                </c:if>
                                <c:forEach var="page" begin="${startPage}" end="${endPage}">
                                    <c:choose>
                                        <c:when test="${page eq curPage}">
                                            <li class="page-item active">
                                                <span class="page-link">${page}</span>
                                            <%
                                                //<a href="/board/showAll/${page}">[${page}]</a><!--<span>[${page}]</span> 감싸면 클릭이 되지 않는다.-->
                                            %>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="page-item">
                                            <a class="page-link" href="/board/showAll/${page}">${page}</a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <c:if test="${curPage<maxPage-5}">
                                    <li class="page-item">
                                        <a class="page-link" href="/board/showAll/${curPage+5}"> > </a>
                                    </li>
                                </c:if>
                                <c:if test="${curPage>=maxPage-5}">
                                    <li class="page-item disabled">
                                        <a class="page-link" href="/board/showAll/${curPage+5}"> > </a>
                                    </li>
                                </c:if>
                                <li class="page-item">
                                    <a class="page-link" href="/board/showAll/${maxPage}"> >> </a>
                                </li>
                            </ul>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

    </div>
</div>
</body>
</html>