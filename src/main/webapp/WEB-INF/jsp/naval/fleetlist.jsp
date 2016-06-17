<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="weather" scope="request" type="java.lang.String"/>
<jsp:useBean id="nation1" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nation2" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="shipList" scope="request" type="java.util.List[]"/>
<h1>Naval Battle Engine: Fleet Selection</h1>
<form:form method="POST" commandName="battleCommand" action="perform" acceptCharset="UTF-8">
    <h3>Weather type: ${weather}</h3>
    <input type="hidden" id="weather" name="weather" value="${weather}"/>
    <table width="100%">
        <thead>
        <tr>
            <th width="50%"><h2>${nation1.name}</h2><input type="hidden" id="nation[0]" name="nation[0]"
                                                           value="${nation1.id}"/></th>
            <th width="50%"><h2>${nation2.name}</h2><input type="hidden" id="nation[1]" name="nation[1]"
                                                           value="${nation2.id}"/></th>
        </tr>
        </thead>
        <tbody>
        <tr valign="top">
            <c:set var="index0" value="0"/>
            <c:forEach items="${shipList}" var="thisList">
                <td width="50%" valign="top">
                    <table class="special">
                        <thead class="special">
                        <tr class="special" class="special">
                            <th class="special" align="left" width=100>Ship Type</th>
                            <th class="special" width=1>Class</th>
                            <th class="special" width=1>Citz</th>
                            <th class="special" width=1>Cost</th>
                            <th class="special" width=1>IndPt</th>
                            <th class="special" width=20>##</th>
                        </tr>
                        </thead>
                        <tbody class="special">
                        <c:set var="index1" value="0"/>
                        <c:forEach items="${thisList}" var="shipType">
                            <tr class="special">
                                <input type="hidden" id="id[${index0}][${index1}]" name="id[${index0}][${index1}]"
                                       value="${shipType.typeId}"/>
                                <td id="name[${index0}][${index1}]" class="special" align="left"
                                    width=100>${shipType.name}</td>
                                <td id="type[${index0}][${index1}]" class="special" width=1>${shipType.shipClass}</td>
                                <td class="special" width=1>${shipType.citizens}</td>
                                <td class="special" width=1>${shipType.cost}</td>
                                <td class="special" width=1>${shipType.indPt}</td>
                                <td class="special" width=20>
                                    <input id="ship[${index0}][${index1}]" name="ship[${index0}][${index1}]" size="2"
                                           value="0"/>
                                </td>
                            </tr>
                            <c:set var="index1" value="${index1+1}"/>
                        </c:forEach>
                        </tbody>
                    </table>
                </td>
                <c:set var="index0" value="${index0+1}"/>
            </c:forEach>
        </tr>
        </tbody>
    </table>
    <input type="submit"/>
</form:form>
