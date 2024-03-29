package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.cs.cs144.AuctionSearchClient;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String q = request.getParameter("q");
        String numResultsToSkip= request.getParameter("numResultsToSkip");
        String numResultsToReturn= request.getParameter("numResultsToReturn");
        SearchResult[] results = null; 
        if (q == null || numResultsToSkip == null || numResultsToReturn == null) {
            request.getRequestDispatcher("./keywordSearch.html").forward(request,response);
            return;
        }
        else {
        Integer intNumResultsToSkip = Integer.parseInt(numResultsToSkip);
        Integer intnumResultsToReturn = Integer.parseInt(numResultsToReturn);

        results= AuctionSearchClient.basicSearch(q, intNumResultsToSkip, intnumResultsToReturn);
        }    
        request.setAttribute("results", results);
        	request.setAttribute("q",q);
        	request.setAttribute("numResultsToSkip", numResultsToSkip);
        	request.setAttribute("numResultsToReturn", numResultsToReturn);
 
        	request.getRequestDispatcher("./searchResults.jsp").forward(request, response);




    }
}
