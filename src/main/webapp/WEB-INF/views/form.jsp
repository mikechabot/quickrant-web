<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Display emotions -->
 <c:if test="${fn:length(emotions) > 0}">
 <div class="container">
   <div class="row margin-bottom-lg text-center">
     <c:forEach items="${emotions}" var="emotion" varStatus="loop">
       <div class="col-lg-4 col-sm-4 col-xs-4">
         <a class="emotion" id="${emotion.emotion}" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/${emotion.emotion}.gif"></a>
       </div>
     </c:forEach>
   </div>
 </div>
 </c:if>
<!-- Display emotions -->
<c:if test="${fn:length(emotions) > 0}">
  <c:forEach items="${emotions}" var="emotion" varStatus="loop">
    <div class="container text-center questions-container"  id="${emotion.emotion}">
      <div class="row margin-bottom-lg">
	    <div class="col-lg-offset-1 col-lg-10">
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
      </div>
    </div>
  </c:forEach>
</c:if>

<div class="container retractable text-center" id="form-container">
  <div class="col-lg-offset-3 col-lg-6 col-sm-offset-2 col-sm-9">
  <div class="panel panel-default row margin-bottom-lg">
    <div class="panel-heading">
      <h3 class="panel-title" id="legend"></h3>
    </div>
    <div class="panel-body">
      <form class="form-inline" role="form" method="post" action="/rant" onSubmit="return validate()">
  	    <input type="hidden" id="pageLoadTime" name="pageLoadTime" value="">
        <input type="hidden" id="emotion" name="emotion" value="">
        <input type="hidden" id="question" name="question" value="">
        <div class="row">
          <div class="col-lg-offset-1 col-lg-5 col-sm-offset-1 col-sm-5">
            <input class="form-control" type="text" id="visitor_name" name="visitor_name" placeholder="Name (optional)">
          </div>
          <div class="col-lg-5 col-sm-5">
            <input class="form-control" type="text" id="location" name="location" placeholder="Location (optional)">
          </div>
        </div>
        <div class="row">
          <div class="col-lg-offset-1 col-lg-10 col-sm-offset-1 col-sm-10">
            <textarea class="form-control" name="rant" id="form-container-textarea" rows="5" placeholder="Say something..."></textarea>
          </div>
        </div>
        <span class="help-block"><span id="counter"></span> characters left.</span>
        <button type="submit" id="submit" class="btn btn-default">Post</button>
      </form>
      <h5 class="text-danger retractable" id="error-text"></h5>
    </div>
    </div>
  </div>
</div>
<div class="container"><hr></div>
