<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>

<form method='post' action='http://forum.eaw1805.com/ucp.php' id='forumRedirect'>
    <input type='hidden' name="mode" value='login'>
    <input type='hidden' name="login" value='eaw'>
    <input type='hidden' name="password" value='eaw1805'>
    <input type='hidden' name="username" value='<%=user.getUsername()%>'>
    <input type='hidden' name="redirect" value='http://www.eaw1805.com/games'>
    <input type='hidden' name="redirectForum" value='http://www.eaw1805.com/games'>
</form>
<script type='text/javascript'>
    r = document.forms['forumRedirect'].elements['redirectForum'];
    if (r) {
        r.value = window.location;
    }

    f = document.getElementById('forumRedirect');
    if (f) {
        f.submit();
    }
</script>
