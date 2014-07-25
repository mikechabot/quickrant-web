<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

SUCCESS: <c:out value="${success}" />
<c:if test="${not empty success}">>
<c:choose>
    <c:when test="${success eq true}">
	  <script>
	    $('#modal-label').text("quickrant");
	    $('div.modal-body p').text("Thanks for speaking your mind.");
	    $('#modal').modal('show');
	     setTimeout(function() {
	         $('#modal').modal('hide');
	     }, 2000);
	  </script>
    </c:when>
    <c:otherwise>
      **FALSE**
	  <script>
	    $('#modal-label').text("Something went wrong...");
	    $('div.modal-body p').html("<h4><a href=\"https://www.google.com/search?q=dealwithit\" target=\"_blank\">#dealwithit</a></h4>");
	    $('#modal').modal('show');
	  </script>
    </c:otherwise>
</c:choose>
</c:if>
<c:set var="success" value="${null}" />

<script>
$('input[name=pageLoadTime]').val((new Date).getTime());
</script>
<script src="/js/rant.js"></script>
<script src="/js/jquery.simplyCountable.js"></script>
<script src="/js/jquery.cookie.js"></script>
</body>
</html>