<%@page import="java.util.*,java.text.*" %>  
<%@page import="edu.ucla.cs.cs144.ItemBean" %>
<%@page import="edu.ucla.cs.cs144.Bid" %>

<html>
<head> 
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<style type="text/css"> 
      html { height: 100% } 
      body { 
        height: 50%;
        margin: 0px;
        padding: 0px;
        background-color:#D6F5FF;
        font-family:"Helvetica Neue",Helvetica,Arial,Verdana,Sans-serif!important;
      } 
      #map_canvas { 
        height: 50%;
        border: 1px solid gray;
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
      .main{
        margin:10px;
         width: 960px;
          display: block;
          margin-left: auto;
          margin-right: auto;
      }
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
                getMap(results[0].geometry.location);            
            }
            else {
            var myOptions = {
                zoom: 3,
                center: new google.maps.LatLng(36.7500, -119.7667),
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
    String buyPrice= ib.getBuyPrice();
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
<title>Stevia's Site: itemID <%= id%></title>
</head>
<body onload="initialize(<%= latitude %>, <%= longitude %>, '<%= location %>')"> 
<div class="main">
  <form action="http://localhost:1448/eBay/item" method="get">
    ItemID <input class="search" type="text" name="id" value="<%= id%>">
    <input class="submit" type="submit" value="Submit">
  </form>

  <h1><%= name%></h1>

  <p>Listed in category: 
  <%for(String cat : categories) {%>
    <%= cat%>, 
  <% } %>
  </p>

  <div>Sale started at: <%= started%></div>
  <div>Sale ends at: <%= ends%></div></br>


  <div>Starting bid: <%= firstBid%></div>
  <div>Current bid: <%=currentBid %> [ <%= numBids%> 
  <% if(numBids.equals("1")) { %>
      bid ]</div>
  <% 
  } else { %>
      bids ]</div>
  <% } 

  if(buyPrice != null) { %>
    <p>Buy Price: <%= buyPrice%></p>
  <% } %>

  <p>Location: <%= location%>, <%= country%>
  <% if(longitude != null) {%>
    (<%= latitude%>, <%= longitude%> )
  <% } %>
  </p>
  <p>Seller: <%= sellerId%> (<%= sellerRating%> &#x2605;)</p>
  <p>Description: <%= description %></p>

<div id="map_canvas" style="width:100%; height:100%"></div>
</div>
</body>

</html>