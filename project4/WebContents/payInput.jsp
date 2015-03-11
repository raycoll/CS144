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
  String id= (String)request.getAttribute("id");
  String name= (String)request.getAttribute("name");
  String buyPrice= (String)request.getAttribute("buyPrice");
%> 

<title>Purchasing <%= id%></title>
</head>

<body> 
<div class="main">
  
  <p>ItemID: <%= id%></p>
  <p>Item Name: <%= name%></p>
  <p>Buy Price: <%= buyPrice%> </p>

 <form action="confirm.jsp">
    Credit Card <input class="search" type="text" name="creditcard">
    <input class="submit" type="submit" value="Submit">
  </form>

</div>
</body>

</html>

