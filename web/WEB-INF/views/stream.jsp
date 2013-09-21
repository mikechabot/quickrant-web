<%@ page language="java"
	import="com.chabot.quickrant.model.Rant"
	import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/rant/getlonglist"/>
<c:set var="rants" value="${sessionScope['longlist']}"/>
<c:if test="${fn:length(rants) > 0}">
<div class="container stream">
	<c:forEach items="${rants}" var="rant">
		<c:if test="${not empty rant}">
		<div class="rant-display-container shadow">
			<div class="rant-display-details pull-left">
				<img src="/img/${rant.getEmotion()}.gif" style="height: 24px; width: 24px;">
			</div>
			<div class="rant-display-text-holder">
				<blockquote class="pull-left">
				<p class="rant-text">${rant.getRant()}</p>
				<small class="rant-info"><b class="rant-author">${rant.getRanter()}</b>, ${rant.getLocation()}<br>
				${rant.getCreated()}</small>
				</blockquote>
			</div>
		</div>
		</c:if>	
	</c:forEach>
</div>
</c:if>    
