<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${success eq true}">
  <c:set var="success" value="${null}" />
  <script>
 	$('#modal-label').text("quickrant");
 	$('div.modal-body p').text("Thanks for speaking your mind.");
	 $('#modal').modal('show');
	 setTimeout(function() {
		 $('#modal').modal('hide');
		 }, 2000);
  </script>
</c:if>
<c:if test="${success eq false}">
  <script>
  	 $('#modal-label').text("Something went wrong...");
  	 $('div.modal-body p').html("https://www.google.com/search?q=dealwithit" target=\"_blank\">#dealwithit</a>");     
	 $('#modal').modal('show');
  </script>
<c:set var="success" value="${null}" />
</c:if>
<script>
$('input[name=pageLoadTime]').val((new Date).getTime());
</script>
<script src="/js/rant.js"></script>
<script src="/js/jquery.simplyCountable.js"></script>
<script src="/js/jquery.cookie.js"></script>
</body>
</html>