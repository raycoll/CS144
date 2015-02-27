<%@page import="java.util.*,java.text.*" %>  
<%@page import="edu.ucla.cs.cs144.ItemBean" %>
<%@page import="edu.ucla.cs.cs144.Bid" %>

<html>
<body>

<%
    ItemBean ib = (ItemBean) request.getAttribute( "info" );
    String id = ib.getId();
    String name = ib.getName();
    String[] categories = ib.getCategories();
    String currentBid = ib.getCurrentBid();
    String firstBid = ib.getFirstBid();
    String numBids = ib.getNumBids();
    Bid[] bids = ib.getBids();
    String latitude = ib.getLatitude();
    String longitude = ib.getLongitude();
    String location = ib.getLocation();
    String country = ib.getCountry();
    String started = ib.getStarted();
    String ends = ib.getEnds();
    String sellerRating = ib.getSellerRating();
    String sellerId = ib.getSellerId();
    String description = ib.getDescription();
%>
  <form action="http://localhost:1448/eBay/item" method="get">
    ItemID: <input type="text" name="id" value="<%= id%>"><br>
    <input type="submit" value="Submit">
  </form>
  <h1><%= name%></h1>
  <p>Sale ends at: <%= ends%></p>
  <p>Current bid: <%=currentBid %> [ <%= numBids%> 
    <% if(numBids.equals("1")) {
    %>
        bid ]</p>
    <% 
    } else {%>
        bids ]</p>
    <% }%>

<%= description %>
</body>

</html>
