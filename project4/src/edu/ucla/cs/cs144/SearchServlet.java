package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
	//http://localhost:1448/eBay/search?q=superman&numResultsToSkip=0&numResultsToReturn=20
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //ServletContext context = getServletContext();

        String q = request.getParameter("q");
        String numResultsToSkip= request.getParameter("numResultsToSkip");
        String numResultsToReturn= request.getParameter("numResultsToReturn");

        Integer intNumResultsToSkip = Integer.parseInt(numResultsToSkip);
        Integer intnumResultsToReturn = Integer.parseInt(numResultsToReturn);
        
        //if(q != null){

        	SearchResult[] results= AuctionSearchClient.basicSearch(q, intNumResultsToSkip, intnumResultsToReturn);
        	request.setAttribute("results", results);
        	request.setAttribute("q",q);
        	request.setAttribute("numResultsToSkip", numResultsToSkip);
        	request.setAttribute("numResultsToReturn", numResultsToReturn);

        	request.getRequestDispatcher("./searchResults.jsp").forward(request, response);
        //}



    }
}
