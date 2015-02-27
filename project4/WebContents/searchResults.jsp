<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="edu.ucla.cs.cs144.AuctionSearchClient" %>
<html>
	<head>
		<title>Stevia's Site</title>
		<style>
			.not-active {
   				pointer-events: none;
   				cursor: default;
			}
		</style>
	</head>
	<body>
		<% 
			SearchResult[] results= (SearchResult[]) request.getAttribute("results");
			String q= (String) request.getAttribute("q");
			String numResultsToSkip = (String) request.getAttribute("numResultsToSkip");
			String numResultsToReturn= (String) request.getAttribute("numResultsToReturn");

			Integer numSkip= Integer.parseInt(numResultsToSkip);
			Integer numReturn = Integer.parseInt(numResultsToReturn);

			Integer numPerPage=10;
		%>
		<form action="http://localhost:1448/eBay/search" method="get">
    		Query: <input type="text" name="q" value="<%= q%>"><br>
    		<input type="hidden" name="numResultsToSkip" value="0">
   			<input type="hidden" name="numResultsToReturn" value="20">
    		<input type="submit" value="Submit">
  		</form>
  		<%
			if(numSkip <= 0){
		%>
				<a href="" class="not-active">Previous</a>
				
		<%	} else {
				
					String prevSkip = Integer.toString(numSkip - numPerPage);
				%>
				<a href="/eBay/search?q=<%= q%>&numResultsToSkip=<%= prevSkip%>&numResultsToReturn=<%= numResultsToReturn%>">Previous</a>
				
		<%
			}
			if(results.length <= numPerPage ) {
		%>
				<a href="" class="not-active">Next</a></br>
			<%} else {
			
				String nextSkip = Integer.toString(numSkip + numPerPage);
			%>
				<a href="/eBay/search?q=<%= q%>&numResultsToSkip=<%= nextSkip%>&numResultsToReturn=<%= numResultsToReturn%>">Next</a></br>
		<%
			}
		
			if(results.length > 0) {
				Integer end;
				if(results.length < numPerPage){
					end = results.length;
				} else {
					end = numPerPage;
				}
				for (int i=0; i<end; i++){
		%>

			<a href="/eBay/item?id=<%= results[i].getItemId()%>"> <%= results[i].getItemId()+ ": " + results[i].getName()%></a></br>

			
			
			<%
				}
			}

			%>

	</body>
</html>
