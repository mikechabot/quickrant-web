<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Display emotions -->
<div class="container emotions-container">
  <c:if test="${fn:length(emotions) > 0}">
    <div class="row text-center">
      <c:forEach items="${emotions}" var="emotion" varStatus="loop">
        <div class="col-md-4"><a class="emotion" id="${emotion.emotion}" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/${emotion.emotion}.gif"></a></div>
      </c:forEach>
    </div>
  </c:if>
</div>

<!-- Display emotions -->
<c:if test="${fn:length(emotions) > 0}">
  <c:forEach items="${emotions}" var="emotion" varStatus="loop">
    <div class="container question-buttons" id="${emotion.emotion}">
      <c:if test="${fn:length(questions) > 0}">
        <c:forEach items="${questions}" var="question" varStatus="loop">
    <c:choose>
	  <c:when test="${emotion.emotion eq 'angry'}">
	    <c:set var="className" value="btn-danger"/>
	  </c:when>
	  <c:when test="${emotion.emotion eq 'sad'}">
	    <c:set var="className" value="btn-primary"/>
	  </c:when>
	  <c:when test="${emotion.emotion eq 'happy'}">
	    <c:set var="className" value="btn-success"/>
	  </c:when>
	</c:choose>
      	<c:if test="${question.emotionId eq emotion.id}">
      	  <button class="btn ${className} question-button" type="button">${question.question}</button>
      	</c:if>
        </c:forEach>
      </c:if>
    </div>
  </c:forEach>
</c:if>

<div class="container form-container retract" id="form-container">
  <div class="panel panel-default">
    <div class="panel-heading">
      <h3 class="panel-title" id="legend"></h3>
    </div>
    <div class="panel-body">
      <form class="form-inline" role="form" method="post" action="/rant" onSubmit="return validate()">
  	    <div style="display: none; visibility: hidden;"><input type="hidden" id="pageLoadTime" name="pageLoadTime" value=""></div>
        <input type="hidden" id="emotion" name="emotion" value="">
        <input type="hidden" id="question" name="question" value="">
        <input class="form-control optional" type="text" id="visitor_name" name="visitor_name" placeholder="Name (optional)">&nbsp;&nbsp;
        <input class="form-control optional" type="text" id="location" name="location" placeholder="Location (optional)">
        <textarea class="form-control" name="rant" id="form-container-textarea" rows="5" placeholder="Say something..."></textarea>
        <span class="help-block"><span id="counter"></span> characters left.</span>
        <button type="submit" id="submit" class="btn btn-default">Post</button>
      </form>
      <p class="text-danger retract" id="error-text"></p>
    </div>
  </div>
</div>