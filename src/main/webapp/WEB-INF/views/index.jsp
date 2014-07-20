<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tags/helper.tld" prefix="helper" %> 
<c:import url="/header.jsp"/>
<c:import url="form.jsp"/>
<c:if test="${fn:length(rants) > 0}">
<div class="container stream">
  <c:forEach items="${rants}" var="rant">
    <c:if test="${not empty rant}">
      <div class="rant-container shadow">
        <div class="rant-details pull-left">
          <img src="/img/${helper:getEmotion(rant.emotionId)}-small.gif">
        </div>
        <div class="rant-text-container">
          <p class="muted pull-left stream-question">${helper:getQuestion(rant.questionId)}</p>
          <a rel="tooltip" data-toggle="tooltip" class="permalink btn btn-mini pull-right" href="/rant/${rant.id}"><i class="icon-plus"></i></a>
          <blockquote class="pull-left wide">
          <p class="rant-text">${rant.rant}</p>
          <small class="rant-info"><b class="dark-bold">${rant.visitorName}</b>, ${rant.location}</small>
          </blockquote>
          <span class="smaller muted pull-right">${rant.created}</span>
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
          <img src="/img/${helper:getEmotion(rant.emotionId)}-small.gif" class="pull-right">
          <h5 class="muted">${helper:getQuestion(rant.questionId)}</h5>
          <blockquote class="pull-left wide rant-single-block">
          <p class="rant-single-text">${rant.rant}</p>
          <small class="rant-single-info"><b class="dark-bold">${rant.visitorName}</b>, ${rant.location}</small>
          </blockquote>
          <span class="smaller muted pull-right">${rant.created}</span>
        </div>
        </div>
    </div>
  </c:if>
</c:if>
<c:import url="footer.jsp"/>
