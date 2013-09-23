<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="success" value="${sessionScope['success']}"/>
<c:if test="${success eq true}">
  <script>
 	$('#modal-label').text("quickrant");
 	$('div.modal-body p').text("Thanks for speaking your mind.");
	 $('#modal').modal('show');
	 setTimeout(function() {
		 $('#modal').modal('hide');
		 }, 2000);
  </script>
<c:set var="success" value="${null}" />
</c:if>
<c:if test="${success eq false}">
  <script>
  	 $('#modal-label').text("Something went wrong...");
  	 $('div.modal-body p').html("<a href=\"https://twitter.com/search?q=%23dealwithit\" target=\"_blank\">#dealwithit</a>");     
	 $('#modal').modal('show');
  </script>
<c:set var="success" value="${null}" />
</c:if>
<script src="/js/rant.js"></script>
<script src="/js/jquery.simplyCountable.js"></script>
</body>
</html>