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
  String isSecure= (String)request.getAttribute("isSecure");
  String badSession= (String)request.getAttribute("badSession");
  if (isSecure.equals("false") || isSecure==null ) {

%>
  <title>Stevia's Site: Not Secure</title>
  </head>
  <body> 
  <div class="main">
    <h2>Request is not secure. Please use a secure channel.</h2>
  </div>
  </body>

</html>
<% } else if (badSession.equals("true")|| badSession==null || session.getAttribute("id") ==null) {
%>
  <title>Stevia's Site: Bad Session</title>
  </head>
  <body> 
  <div class="main">
    <h2>Bad Session.</h2>
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
  <h1>Purchase Confirmed</h1>
  <p>ItemID: <%= id%></p>
  <p>Item Name: <%= name%></p>
  <p>Buy Price: <%= buyPrice%> </p>
  <p>Credit Card: <%= cardNum%> </p>
  <p>Time: <%= date%></p>

</div>
</body>

</html>
<% } %>
