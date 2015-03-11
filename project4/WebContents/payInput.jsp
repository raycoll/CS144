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


%> 


<title>Purchasing <%= id%></title>
</head>

<body> 
<div class="main">
  
  <h2>Purchasing</h2>
  <p>ItemID: <%= id%></p>
  <p>Item Name: <%= name%></p>
  <p>Buy Price: <%= buyPrice%> </p>

 <form action="https://localhost:8443/eBay/confirm" method="get">
    Credit Card <input class="search" type="text" name="cardNum">
    <input class="submit" type="submit" value="Submit">
  </form>

</div>
</body>

</html>
<%
}
%>

