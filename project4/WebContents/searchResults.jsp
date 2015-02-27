<%@ page import "edu.ucla.cs.cs144.SearchResult" %>
<html>
	<head>
		<title>Welcome to Stevia's Site</title>
	</head>
	<body>
		<% 
			SearchResult[] results= request.getAttribute("results");
			String q= request.getAttribute("q");
			String numResultsToSkip = request.getAttribute("numResultsToSkip");
			String numResultsToReturn= request.getAttribute("numResultsToReturn");


			if(results.length > 0) {

				for (SearchResult result : results) {
		%>
			<p>hi</p>
					//<a href="/eBay/item?id=<%= result.getItemId()%>"> <%= results.getItemId()+ ": " + result.getName()%></a>

			//<a href="/eBay/search/?q=<%= q%>&numResultsToSkip=<%= numResultsToSkip%>&numResultsToReturn=<%= numResultsToReturn%>">Previous</a>
			<%
				}
			}

			%>
			<!--//<p><% = q %></p>-->

	</body>
</html>