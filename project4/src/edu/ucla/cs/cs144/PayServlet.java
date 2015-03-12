package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.cs.cs144.AuctionSearchClient;
import javax.servlet.http.HttpSession;

public class PayServlet extends HttpServlet implements Servlet {
       
    public PayServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        

        String id = (String)session.getAttribute("id");
        String name= (String)session.getAttribute("name");
        String buyPrice= (String)session.getAttribute("buyPrice");

        if (id == null || name == null || buyPrice == null) {
            request.setAttribute("badSession", "true");
        } else {
            request.setAttribute("badSession", "false");

            request.setAttribute("id",id);
            request.setAttribute("name", name);
            request.setAttribute("buyPrice", buyPrice);
        }

        request.getRequestDispatcher("./payInput.jsp").forward(request, response);
        
    }
}
