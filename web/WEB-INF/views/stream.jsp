<%@ page language="java"
import="com.chabot.quickrant.model.Rant"
import="java.util.List"
%>
<jsp:include page="/rant/list"/>
<% List<Rant> rants = (List<Rant>) request.getSession().getAttribute("rants"); %>
<% if(rants != null && !rants.isEmpty()) { %>
 		<div class="container stream"> <%
		for(Rant rant : rants) {
			if(rant != null) { %>
			<div class="rant-display-container">
				<div class="rant-display-details">
					<img src="/img/<%=rant.getEmotion()%>.gif" style="height: 24px; width: 24px;"><br>
					<%=rant.getCreated()%>
				</div>
				<div class="rant-display-text">
					<blockquote class="pull-right">
					<p><%=rant.getRant()%></p>
					<small><b><%=rant.getRanter()%></b>, <%=rant.getLocation()%></small>
					</blockquote>
				</div>
			</div>			
<%			}
		} %>
 		</div>
<% 	} %>	    