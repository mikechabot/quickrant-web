<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tags/helper.tld" prefix="helper" %>

<c:if test="${not empty rant}">
  <div class="rant-container shadow">
    <div class="rant-single-text-container">
      <img src="/img/${helper:getEmotion(rant.emotionId)}-small.gif" class="pull-right">
      <h5 class="muted">${helper:getQuestion(rant.questionId)}</h5>
      <blockquote class="pull-left wide rant-single-block">
        <p class="rant-text">${rant.rant}</p>
        <small class="rant-info"><b class="dark-bold">${rant.visitorName}</b>, ${rant.location}</small>
      </blockquote>
      <span class="smaller muted pull-left">Rant #${rant.id}</span>
      <span class="smaller muted pull-right">${rant.created}</span>
    </div>
  </div>
</c:if>