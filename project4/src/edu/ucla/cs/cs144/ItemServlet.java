package edu.ucla.cs.cs144;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}
    
    private Bid parseBid(Element bid) {
        Bid b = new Bid();
        b.setBidderRating(bid.getAttribute("Rating"));
        b.setBidderId(bid.getAttribute("UserID"));
        b.setBidderLocation(bid.getElementsByTagName("Location").item(0).getFirstChild().getNodeValue());
        b.setBidderCountry(bid.getElementsByTagName("Country").item(0).getFirstChild().getNodeValue());
        b.setTime(bid.getElementsByTagName("Time").item(0).getFirstChild().getNodeValue());
        b.setAmount(bid.getElementsByTagName("Amount").item(0).getFirstChild().getNodeValue());
        return b;
    }
    private ItemBean getItemBeanFromXml(String xml) {
        ItemBean ib = new ItemBean();
        try {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream s = new ByteArrayInputStream(xml.getBytes());
        Document doc = builder.parse(s);
        
        Element item = doc.getDocumentElement();
        
        // set item id
        ib.setId(item.getAttribute("ItemID"));
        
        // set name
        ib.setName(doc.getElementById("Name").getFirstChild().getNodeValue());

        // add categories
        NodeList cats = doc.getElementsByTagName("Category");
        for (int i = 0; i < cats.getLength(); ++i) {
           ib.addCategory(cats.item(i).getFirstChild().getNodeValue()); 
        }
        
        // add current bid
        ib.setCurrentBid(doc.getElementById("Currently").getFirstChild().getNodeValue());

        // add first bid
        ib.setFirstBid(doc.getElementById("First Bid").getFirstChild().getNodeValue());

        // add num bids
        ib.setNumBids(doc.getElementById("Number_of_Bids").getFirstChild().getNodeValue());
        
        // add bids
        NodeList bids = doc.getElementById("Bids").getElementsByTagName("Bid");
        for( int i = 0; i < bids.getLength(); ++i) {
           Bid b = parseBid((Element) bids.item(i));
           ib.addBid(b);
        } 

        // add location
        Element location = doc.getElementById("Location");
        String latitude = location.getAttribute("Latitude");
        if (!latitude.equals("")) {
            ib.setLatitude(latitude);
        }
        String longitude = location.getAttribute("Longitude");
        if (!longitude.equals("")) {
            ib.setLongitude(longitude);
        }
        
        ib.setLocation(location.getFirstChild().getNodeValue());
        
        // add country
        Element country = doc.getElementById("Country");
        ib.setCountry(country.getFirstChild().getNodeValue());

        // add started
        ib.setStarted(doc.getElementById("Started").getFirstChild().getNodeValue());

        // add Ends
        ib.setEnds(doc.getElementById("Ends").getFirstChild().getNodeValue());

        // add seller rating and id
        Element seller = doc.getElementById("Seller");
        ib.setSellerRating(seller.getAttribute("Rating"));
        ib.setSellerId(seller.getAttribute("UserId"));

        // add description
        ib.setDescription(doc.getElementById("Description").getFirstChild().getNodeValue());
        } catch(Exception e) {
            System.out.println("Failed to parse!");
            return null;
        }

        return ib;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	String itemId = request.getParameter("id");
	String itemXml = AuctionSearchClient.getXMLDataForItemId(itemId);
	PrintWriter out	= response.getWriter();	
	request.setAttribute("info",getItemBeanFromXml(itemXml));
	RequestDispatcher rd = request.getRequestDispatcher("./itemInfo.jsp");
	rd.forward(request,response);
	
	
    }
}
