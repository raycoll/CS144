<%@page import="java.util.*,java.text.*" %>  
<%@page import="edu.ucla.cs.cs144.ItemBean" %>
<%@page import="edu.ucla.cs.cs144.Bid" %>

<html>
<head> 
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<style type="text/css"> 
      html { height: 100% } 
      body { height: 50%; margin: 0px; padding: 0px } 
      #map_canvas { height: 50% } 
</style> 
<script type="text/javascript" 
    src="http://maps.google.com/maps/api/js?sensor=false"> 
</script> 
<script type="text/javascript">
function getMap(latlng) {
    var myOptions = { 
        zoom: 10, // default is 8  
        center: latlng, 
        mapTypeId: google.maps.MapTypeId.ROADMAP 
     }; 
    var map = new google.maps.Map(document.getElementById("map_canvas"), 
                                  myOptions); 
}

function getLatLng(loc) {
    var geocoder = new google.maps.Geocoder();
    geocoder.geocode( { 'address': loc}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                var myOptions = { 
                    zoom: 4, // default is 8  
                    center: results[0].geometry.location,
                    mapTypeId: google.maps.MapTypeId.ROADMAP 
                }; 
                var map = new google.maps.Map(document.getElementById("map_canvas"), 
                                  myOptions);            
            }
    });
}
function initialize(lat, long, loc) {
      if (!lat || !long) {
         getLatLng(loc);
      }
      else {
        var latlng = new google.maps.LatLng(lat,long);
        getMap(latlng);      
      }
} 

</script> 
<%
    ItemBean ib = (ItemBean) request.getAttribute( "info" );
    String id = ib.getId();
    String name = ib.getName();
    String[] categories = ib.getCategories();
    String currentBid = ib.getCurrentBid();
    String firstBid = ib.getFirstBid();
    String numBids = ib.getNumBids();
    /*String buyPrice= ib.getBuyPrice();*/
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
    String buyPrice = ib.getBuyPrice();
%>
<title>Stevia's Site: itemID <%= id%></title>
</head>
<body onload="initialize(<%= latitude %>, <%= longitude %>, '<%= country %>')"> 

  <form action="http://localhost:1448/eBay/item" method="get">
    ItemID: <input type="text" name="id" value="<%= id%>"><br>
    <input type="submit" value="Submit">
  </form>

  <h1><%= name%></h1>

  <p>Listed in category: 
  <%for(String cat : categories) {%>
    <%= cat%>, 
  <% } %>
  </p>

  <p>Sale starts at: <%= started%></p>
  <p>Sale ends at: <%= ends%></p>


  <p>Starting bid: <%= firstBid%></p>

  <p>Current bid: <%=currentBid %> [ <%= numBids%> 
  <% if(numBids.equals("1")) { %>
      bid ]</p>
  <% 
  } else { %>
      bids ]</p>
  <% } %>

  <p>Location: <%= location%>, <%= country%>
  <% if(!longitude.equals("NULL")) {%>
    (<%= latitude%>, <%= longitude%> )
  <% } %>
  </p>
  <p>Seller: <%= sellerId%> (<%= sellerRating%>)</p>
  <%= description %><br><br>

<div id="map_canvas" style="width:100%; height:100%"></div>
</body>

</html>
