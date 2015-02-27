<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
	<head>
		<title>Welcome to Stevia's Site</title>
	</head>
	<body>
		<% 
			SearchResult[] results= (SearchResult[]) request.getAttribute("results");
			String q= (String) request.getAttribute("q");
			String numResultsToSkip = (String) request.getAttribute("numResultsToSkip");
			String numResultsToReturn= (String) request.getAttribute("numResultsToReturn");


			if(results.length > 0) {

				for (SearchResult result : results) {
		%>
			<p>hi</p>
					//<a href="/eBay/item?id=<%= result.getItemId()%>"> <%= results[0].getItemId()+ ": " + results[0].getName()%></a>

			//<a href="/eBay/search/?q=<%= q%>&numResultsToSkip=<%= numResultsToSkip%>&numResultsToReturn=<%= numResultsToReturn%>">Previous</a>
			<%
				}
			}

			%>

	</body>
</html>
