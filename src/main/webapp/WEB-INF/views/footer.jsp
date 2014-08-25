<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="modal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 id="modal-title" class="modal-title"></h4>
      </div>
      <div class="modal-body">
        <p></p>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<c:if test="${not empty success}">>
<c:choose>
    <c:when test="${success eq true}">
	  <script>
	    $('#modal-title').text("quickrant");
	    $('div.modal-body p').html("<h5>Thanks for speaking your mind.</h5>");
	    $('#modal').modal('show');
	     setTimeout(function() {
	         $('#modal').modal('hide');
	     }, 60000);
	  </script>
    </c:when>
    <c:otherwise>
	  <script>
	    $('#modal-title').text("Something went wrong...");
	    $('div.modal-body p').html("<h4><a href=\"https://www.google.com/search?q=dealwithit\" target=\"_blank\">#dealwithit</a></h4>");
	    $('#modal').modal('show');
	  </script>
    </c:otherwise>
</c:choose>
</c:if>
<c:set var="success" value="${null}" />
<script src="/js/rant.js"></script>
<script src="/js/jquery.jscroll.min.js"></script>
<script src="/js/jquery.simplyCountable.js"></script>
<script src="/js/jquery.cookie.js"></script>
</body>
</html>