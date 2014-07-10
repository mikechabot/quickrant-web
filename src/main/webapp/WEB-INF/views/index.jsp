<%@ page language="java"
	import="com.quickrant.rave.model.Rant"
	import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:import url="header.jsp"/>
<c:import url="form.jsp"/>
<c:if test="${fn:length(rants) > 0}">
<div class="container stream">
	<c:forEach items="${rants}" var="rant">
		<c:if test="${not empty rant}">
		<div class="rant-container shadow">
			<div class="rant-details pull-left">
				<img src="/img/${rant.getEmotion()}.gif" style="height: 32px; width: 32px;">
			</div>
			<div class="rant-text-container">
				<p class="muted pull-left stream-question">${rant.getQuestion()}</p>
				<a rel="tooltip" data-toggle="tooltip" class="permalink btn btn-mini pull-right" href="/rant/${rant.getId()}"><i class="icon-plus"></i></a>
				<blockquote class="pull-left wide">
				<p class="rant-text">${rant.getRant()}</p>
				<small class="rant-info"><b class="dark-bold">${rant.getRanter()}</b>, ${rant.getLocation()}</small>
				</blockquote>
				<span class="smaller muted pull-right">${rant.getCreated()}</span>
			</div>
		</div>
		</c:if>	
	</c:forEach>
</div>
</c:if>
<c:if test="${fn:length(rants) == 0}">
	<c:if test="${not empty rant}">
		<div class="container single-stream">
			<div class="rant-container shadow">
				<div class="rant-single-text-container">
					<img src="/img/${rant.getEmotion()}.gif" class="pull-right" style="height: 32px; width: 32px;">
					<h5 class="muted">${rant.getQuestion()}</h5>
					<blockquote class="pull-left wide rant-single-block">
					<p class="rant-single-text">${rant.getRant()}</p>
					<small class="rant-single-info"><b class="dark-bold">${rant.getRanter()}</b>, ${rant.getLocation()}</small>
					</blockquote>
					<span class="smaller muted pull-right">${rant.getCreated()}</span>
				</div>
			</div>
		</div>
	</c:if>
</c:if>
<c:import url="footer.jsp"/>