<%@page import="java.util.*,java.text.*" %>  

<html>

<head>  
<link rel="stylesheet" href="style.css" type="text/css">
<style type="text/css"> 
      html { height: 100% } 
      body { 
        height: 50%;
        margin: 0px;
        padding: 0px;
      } 
      tr, td, th {
        border-bottom: 1px solid #e2e2e2 !important;
      }
      button {
        margin:10px;
      }
      h2 {
        text-align: center;
      }
</style> 
<%
  String badSession= (String)request.getAttribute("badSession");
  if (badSession.equals("true")|| badSession==null ) {
%>
  <title>Bad Session</title>
  </head>
  <body> 
  <div class="main">
    <h2>Bad Session. To purchase item, please press "Pay Now" on the item page.</h2>
  </div>
  </body>

</html>
<% 
} else {


  String id= (String)request.getAttribute("id");
  String name= (String)request.getAttribute("name");
  String buyPrice= (String)request.getAttribute("buyPrice");
  String cardNum= (String)request.getAttribute("cardNum");

  SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
  Date dt = new Date();
  String date = sdf.format(dt);
%> 

<title>Confirm Purchase for <%= id%></title>
</head>

<body> 
<div class="main">
  <h1>Confirm Purchase</h1>
  <p>ItemID: <%= id%></p>
  <p>Item Name: <%= name%></p>
  <p>Buy Price: <%= buyPrice%> </p>
  <p>Credit Card: <%= cardNum%> </p>
  <p>Time: <%= date%></p>

</div>
</body>

</html>
<% } %>
