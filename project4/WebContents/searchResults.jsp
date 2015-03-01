<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="edu.ucla.cs.cs144.AuctionSearchClient" %>
<html>
	<head>
		<title>Stevia's Site Search Results</title>
		<link rel="stylesheet" href="style.css" type="text/css">
		<style>
			.item-links a:hover {
				text-decoration: underline;
			}
			.nav a:visited {
				color:#0654ba;
			}
			.nav {
				float:right;
			}
			#next {
				margin-left:20px;
			}
			.search {
				width:400px;
				height:30px;
				font-size: 16px;
			}
			.not-active {
   				pointer-events: none;
   				cursor: default;
   				color:#d3d3d3 !important;
			}
		</style>
		<script type="text/javascript">
		     window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("txt1")); 
            }	
		</script>
		<script type="text/javascript" src="autosuggest.js"></script>
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
		<div class=main>
		<form action="http://104.236.169.138:8080/eBay/search" method="get">
    		Search <input id="txt1" class="search" type="text" name="q" value="<%= q%>">
    		<input type="hidden" name="numResultsToSkip" value="0">
   			<input type="hidden" name="numResultsToReturn" value="20">
    		<input class="submit" type="submit" value="Submit">
  		</form>
  		<div class="nav">
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
				<a id="next" href="" class="not-active">Next</a></br>
			<%} else {
			
				String nextSkip = Integer.toString(numSkip + numPerPage);
			%>
				<a id="next" href="/eBay/search?q=<%= q%>&numResultsToSkip=<%= nextSkip%>&numResultsToReturn=<%= numResultsToReturn%>">Next</a>
		<%
			}
		%>
		</div></br>
		<div class="item-links">
		<%
			if(results.length > 0) {
				Integer end;
				if(results.length < numPerPage){
					end = results.length;
				} else {
					end = numPerPage;
				}
				for (int i=0; i<end; i++){
		%>

			<a href="/eBay/item?id=<%= results[i].getItemId()%>"> <%= results[i].getItemId()+ ": " + results[i].getName()%></a></br></br>

			
			
			<%
				}
			}

			%>
			</div>
		</div>
	</body>
</html>
