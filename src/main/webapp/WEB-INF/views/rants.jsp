<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tags/helper.tld" prefix="helper" %>
<c:forEach items="${rants}" var="rant">
  <c:if test="${not empty rant}">
	<div class="row panel">
	  <div class="col-lg-offset-3 col-lg-6 col-sm-offset-2 col-sm-9">
	  <div class="panel-heading">
	    <div class="row">
	      <div class="col-lg-1 col-sm-1 col-xs-2">
	        <img src="/img/${helper:getEmotion(rant.emotionId)}-small.gif">
	      </div>
	      <div class="col-lg-10 col-sm-10 col-xs-8">
	        <h2 class="panel-title">${helper:getQuestion(rant.questionId)}</h2>
	      </div>
	      <div class="col-lg-1 col-sm-1 col-xs-2 text-right">
	        <a href="/rant/${rant.id}" class="permalink glyphicon glyphicon-leaf"></a>
	      </div>
	    </div>
	  </div>
	  <div class="panel-body">
        <blockquote>
        <p class="rants-text">${rant.rant}</p>
        <small><span class="mild-bold">${rant.visitorName}</span> // ${rant.location} // <span class="rant-created timeago" title="${rant.created}"></span></small>
        </blockquote>
	  </div>
	  <hr>
      </div>
	</div>	
  </c:if>
</c:forEach>
<div class="text-center">
<a href="ajax/${maxId}" class="next-selector btn-default btn btn-lg">More</a>
</div>