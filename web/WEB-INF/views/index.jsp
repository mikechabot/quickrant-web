<%@ page language="java"
	import="com.chabot.quickrant.model.Rant"
	import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="header.jsp"/>
<jsp:include page="form.jsp" />
<c:if test="${fn:length(rants) > 0}">
<div class="container stream">
	<c:forEach items="${rants}" var="rant">
		<c:if test="${not empty rant}">
		<div class="rant-display-container shadow">
			<div class="rant-display-details pull-left">
				<a href="#" class="question-popover" id="${rant.getId()}" rel="popover"><img src="/img/${rant.getEmotion()}.gif" style="height: 24px; width: 24px;"></a>	
				<script>				
				$("#${rant.getId()}").popover({ content: "${rant.getQuestion()}", placement: "left", trigger: "hover"});
				</script>			
			</div>
			<div class="rant-display-text-holder">
				<blockquote class="pull-left wide">
				<p class="rant-text">${rant.getRant()}</p>
				<small class="rant-info"><b class="rant-author">${rant.getRanter()}</b>, ${rant.getLocation()}</small>
				<span class="smaller muted pull-right">${rant.getCreated()}</span>
				</blockquote>
			</div>
		</div>
		</c:if>	
	</c:forEach>
</div>
</c:if> 
<jsp:include page="footer.jsp" />	