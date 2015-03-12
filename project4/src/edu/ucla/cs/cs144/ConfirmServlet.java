package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.cs.cs144.AuctionSearchClient;
import javax.servlet.http.HttpSession;

public class ConfirmServlet extends HttpServlet implements Servlet {
       
    public ConfirmServlet() {}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);

        String id = (String)session.getAttribute("id");
        String name= (String)session.getAttribute("name");
        String buyPrice= (String)session.getAttribute("buyPrice");

        String cardNum = request.getParameter("cardNum");

        if (!request.isSecure()){
            request.setAttribute("isSecure", "false");
        } else if (id == null || name == null || buyPrice == null || cardNum==null) {
            request.setAttribute("badSession", "true");
            request.setAttribute("isSecure", "true");
        } else {
            request.setAttribute("isSecure", "true");
            request.setAttribute("badSession", "false");
            request.setAttribute("id",id);
            request.setAttribute("name", name);
            request.setAttribute("buyPrice", buyPrice);
            request.setAttribute("cardNum",cardNum);
        }

        request.getRequestDispatcher("./confirm.jsp").forward(request, response);
        
    }
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);

        String id = (String)session.getAttribute("id");
        String name= (String)session.getAttribute("name");
        String buyPrice= (String)session.getAttribute("buyPrice");

        String cardNum = request.getParameter("cardNum");

        if (!request.isSecure()){
            request.setAttribute("isSecure", "false");
        } else if (id == null || name == null || buyPrice == null || cardNum==null) {
            request.setAttribute("badSession", "true");
            request.setAttribute("isSecure", "true");
        } else {
            request.setAttribute("isSecure", "true");
            request.setAttribute("badSession", "false");
            request.setAttribute("id",id);
            request.setAttribute("name", name);
            request.setAttribute("buyPrice", buyPrice);
            request.setAttribute("cardNum",cardNum);
        }

        request.getRequestDispatcher("./confirm.jsp").forward(request, response);
        
    }
}
