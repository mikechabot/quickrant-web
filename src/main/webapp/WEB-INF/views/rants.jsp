<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tags/helper.tld" prefix="helper" %>

<c:forEach items="${rants}" var="rant">
  <c:if test="${not empty rant}">
   <!-- 
    <div class="rant-container shadow">
      <div class="rant-details pull-left">
        <img src="/img/${helper:getEmotion(rant.emotionId)}-small.gif">
      </div>
     
     -->
	<div class="panel panel-default shadow">
	  <div class="panel-heading">
	    <h3 class="panel-title"><img src="/img/${helper:getEmotion(rant.emotionId)}-small.gif">&nbsp;&nbsp;${helper:getQuestion(rant.questionId)}</h3>
	  </div>
	  <div class="panel-body">
        <blockquote class="pull-left wide rant-blockquote">
        <p class="rants-text">${rant.rant}</p>
        <small><b class="dark-bold">${rant.visitorName}</b>, ${rant.location}</small>
        </blockquote>
	  </div>
	  <div class="panel-footer"><small>${rant.created}</small></div>
	</div>	
	
  </c:if>
</c:forEach>

<a class="next-selector" href="ajax/${maxId}"><button type="button" class="btn btn-default">Moar!</button></a>

