<%@ page import="org.springframework.beans.support.PagedListHolder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="user" scope="request" class="com.eaw1805.data.model.User"/>
<jsp:useBean id="userDateJoined" scope="request" class="java.util.Date"/>
<jsp:useBean id="followingCnt" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="followingList" scope="request" type="java.util.List"/>
<jsp:useBean id="followersCnt" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="followersList" scope="request" type="java.util.List"/>
<jsp:useBean id="watchCnt" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="gameList" scope="request" type="java.util.List"/>
<jsp:useBean id="profileUser" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="messagesList" scope="request" type="org.springframework.beans.support.PagedListHolder"/>

<script type="text/javascript">
    $.blockUI.defaults.applyPlatformOpacityRules = false;

    function changeAvatar() {
        window.location = "http://gravatar.com/emails/";
    }

    function openGoToPageForm() {
        $.blockUI({ message:$('#goToPage'), css:{ width:'255px', height:'90px' } });
    }

    function goToPage() {
        var page = $('#pageNumber').val();
        if (page !== null && !isNaN(page) && page == Math.floor(page) && page > 0 && page <= ${messagesList.pageCount}) {
            page--;
            document.location.href = '<c:url value="/inbox?page="/>' + page;
        }
        $.unblockUI();
    }

    $(document).ready(function () {
        $('#no').click(function () {
            $.unblockUI();
            return false;
        });
    });

</script>
<%!

    List<Integer> getNavigationMenu(final PagedListHolder messagesList) {

        final List<Integer> navigationMenu = new ArrayList<Integer>();

        final int currentPage = messagesList.getPage();
        final int numberOfPages = messagesList.getPageCount();

        if (messagesList.isFirstPage()) {
            if (numberOfPages > 5) {
                for (int i = 0; i < 4; i++) {
                    navigationMenu.add(i);
                }
                navigationMenu.add(-1);
                navigationMenu.add(numberOfPages - 1);
            } else {
                for (int i = 0; i < numberOfPages; i++) {
                    navigationMenu.add(i);
                }
            }
        } else if (messagesList.isLastPage()) {
            if (numberOfPages > 5) {
                navigationMenu.add(0);
                navigationMenu.add(-1);
                for (int i = numberOfPages - 4; i < numberOfPages; i++) {
                    navigationMenu.add(i);
                }
            } else {
                for (int i = 0; i < numberOfPages; i++) {
                    navigationMenu.add(i);
                }
            }
        } else if (numberOfPages < 5) {
            for (int i = 0; i < numberOfPages; i++) {
                navigationMenu.add(i);
            }
        } else if (numberOfPages > 5) {

            for (int i = currentPage - 1; i <= currentPage + 1; i++) {
                if (i >= 0 && i <= numberOfPages) {
                    navigationMenu.add(i);
                }
            }

            if (navigationMenu.get(0) != 0) {
                if (navigationMenu.get(1) != 2) {
                    navigationMenu.add(0, -1);
                }
                navigationMenu.add(0, 0);
            }

            if (navigationMenu.get(navigationMenu.size() - 1) != numberOfPages && navigationMenu.get(navigationMenu.size() - 1) <= numberOfPages - 2) {
                if (navigationMenu.get(navigationMenu.size() - 1) != numberOfPages - 2) {
                    navigationMenu.add(-1);

                }
                navigationMenu.add(numberOfPages - 1);

            }

            if (navigationMenu.size() == 5) {
                if(navigationMenu.get(navigationMenu.size()-2) == -1){
                    navigationMenu.add(3,3);
                }

                if(navigationMenu.get(1) == -1){
                    navigationMenu.add(2,numberOfPages-4);
                }
            }

        }

        return navigationMenu;
    }
%>
<div class="player" style="height: 200px;">
    <h1 class="player">
        <img src="https://secure.gravatar.com/avatar/${profileUser.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
             class="avatarLink" alt="" height="48" width="48"
                <c:if test="${profileUser.userId == user.userId}">
                    onclick="changeAvatar();"
                    OnMouseOver="this.style.cursor='pointer';"
                    OnMouseOut="this.style.cursor='default';"
                </c:if>
                />
        <c:out value="${profileUser.username}"/>
    </h1>

    <h2 class="player">
        ${profileUser.fullname}<br>
        ${profileUser.location}<br>
        Member Since <fmt:formatDate value="${userDateJoined}" type="both" pattern="MMM d, yyyy"/>
    </h2>

    <h3 class="player">
        <a href="<c:url value="/settings"/>">Following<br>${followingCnt} players</a>
        <ul class="avatars">
            <c:forEach items="${followingList}" var="thisUser">
                <li>
                    <a href="<c:url value="/user/${thisUser.username}"/>" title="${thisUser.username}"><img
                            src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="" height="24" width="24"></a>
                </li>
            </c:forEach>
        </ul>

        <br>
        <a href="<c:url value="/user/${profileUser.username}/followers"/>">${followersCnt} followers</a>
        <ul class="avatars">
            <c:forEach items="${followersList}" var="thisUser">
                <li>
                    <a href="<c:url value="/user/${thisUser.username}"/>" title="${thisUser.username}"><img
                            src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="" height="24" width="24"></a>
                </li>
            </c:forEach>
        </ul>

        <a href="<c:url value="/settings"/>">Watching<br>games</a>
        <ul class="avatars">
            <c:forEach items="${gameList}" var="thisGame">
                <li class="watch"><a href="<c:url value="/scenario/${thisGame.scenarioIdToString}/game/${thisGame.gameId}/info"/>"
                                     title="Game ${thisGame.gameId}">${thisGame.gameId}</a></li>
            </c:forEach>
        </ul>
    </h3>

    <h4 class="pagination">
        ${messagesList.nrOfElements} Threads • <a href="javascript:openGoToPageForm()">Page ${messagesList.page + 1 }
        of ${messagesList.pageCount} </a> •

        <c:forEach items="<%=getNavigationMenu(messagesList)%>" var="thisPage">

            <c:if test="${thisPage == messagesList.page  }">
                <a class="current" href='<c:url value="/inbox?page=${thisPage}"/>'> ${thisPage+1}</a>
            </c:if>
            <c:if test="${thisPage != messagesList.page  }">
                <c:if test="${thisPage != -1 }">
                    <a class="others" href='<c:url value="/inbox?page=${thisPage}"/>'> ${thisPage+1}</a>
                </c:if>
                <c:if test="${thisPage == -1 }">
                    <custom style="font-size: 10px; margin: 0px;">...</custom>
                </c:if>
            </c:if>
        </c:forEach>

    </h4>
</div>

<div id="goToPage" style="display:none; cursor: default;">

    Enter the page number you wish to go to: </br>

    <input type="text" id="pageNumber" value="0" size="20"/></br>
    </br>
    <div>
        <input style="float: right; " type="button" value="Ok" onclick="goToPage();"/>
        <input style="float: right;" type="button" id="no" value="Cancel"/>
    </div>


</div>
