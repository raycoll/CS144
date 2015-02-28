package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       try {
       PrintWriter out = response.getWriter();

       // get query string
       String q = request.getParameter("q");
       
       // Send query to Google Suggest
       String suggestApiEndpoint = "http://google.com/complete/search?output=toolbar&q=";
       URL googleReqURL = new URL(suggestApiEndpoint + q);
       HttpURLConnection suggestConn = (HttpURLConnection) googleReqURL.openConnection();
       suggestConn.setRequestMethod("GET");
       suggestConn.connect();

       // Send back the Google XML response 
       BufferedReader reader = new BufferedReader(new InputStreamReader(suggestConn.getInputStream())); 
       StringBuilder xmlStr = new StringBuilder();
       String line;
       while ((line = reader.readLine()) != null) {
            xmlStr.append(line);
       }

       out.print(xmlStr);
       reader.close();
       }
       catch (Exception e) {
         System.out.println(e.toString());
       }
    }
}
