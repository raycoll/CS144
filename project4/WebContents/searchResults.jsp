<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="edu.ucla.cs.cs144.AuctionSearchClient" %>
<html>
	<head>
		<title>Stevia's Site Search Results</title>
		<style>
			body {
				background-color:#D6F5FF;
				font-family:"Helvetica Neue",Helvetica,Arial,Verdana,Sans-serif!important;
			}
			a {
				text-decoration: none;
			}
			div.suggestions {
    			-moz-box-sizing: border-box;
    			box-sizing: border-box;
    			border: 1px solid black;
    			position: absolute; 
    			background-color:white;
			}
			div.suggestions div {
    			cursor: default;
    			padding: 0px 3px;
			}
			div.suggestions div.current {
    			background-color: #3366cc;
    			color: white;
			}
			.item-links a:hover {
				text-decoration: underline;
			}
			.nav a:visited {
				color:#0654ba;
			}
			.main{
				margin:10px;
				width: 960px;
  				display: block;
  				margin-left: auto;
  				margin-right: auto;
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

			.submit {
				font-size: 16px;
				height:25px;
				text-shadow: 0 1px 0 rgba(0,0,0,.2);
				border-radius: 3px;
				border: 0;
				color: #fff;
				text-shadow: 0 1px 0 rgba(0,0,0,0.17);
				background-color: #00509d;
				background-image: -webkit-gradient(linear,left bottom,left top,color-stop(0,#00509d),color-stop(1,#007abd));
				background-image: -ms-linear-gradient(bottom,#00509d,#007abd);
				background-image: -moz-linear-gradient(center bottom,#00509d 0,#007abd 100%);
				background-image: -o-linear-gradient(#007abd,#00509d);
				background-image: linear-gradient(to top,#00509d,#007abd);
				filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#007abd',endColorstr='#00509d',GradientType=0);
				-webkit-box-shadow: 0 2px 0 0 rgba(0,0,0,0.06);
				-moz-box-shadow: 0 2px 0 0 rgba(0,0,0,0.06);
				box-shadow: 0 2px 0 0 rgba(0,0,0,0.06);
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
