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
        if (bid == null) {
          return b;
        }
        Element bidder = (Element) bid.getElementsByTagName("Bidder").item(0);
        b.setBidderRating(bidder.getAttribute("Rating"));
        b.setBidderId(bidder.getAttribute("UserID"));
        b.setBidderLocation(bidder.getElementsByTagName("Location").item(0).getFirstChild().getNodeValue());
        NodeList countryTextNodes = bidder.getElementsByTagName("Country").item(0).getChildNodes();
        if (countryTextNodes.getLength() != 0) {
          b.setBidderCountry(countryTextNodes.item(0).getNodeValue());
        }
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
        
        ib.setName(item.getElementsByTagName("Name").item(0).getFirstChild().getNodeValue());
        // add categories
        NodeList cats = item.getElementsByTagName("Category");
        for (int i = 0; i < cats.getLength(); ++i) {
           ib.addCategory(cats.item(i).getFirstChild().getNodeValue()); 
        }
        // add current bid
        ib.setCurrentBid(item.getElementsByTagName("Currently").item(0).getFirstChild().getNodeValue());

        // add first bid
        ib.setFirstBid(item.getElementsByTagName("First_Bid").item(0).getFirstChild().getNodeValue());

        // add num bids
        ib.setNumBids(item.getElementsByTagName("Number_of_Bids").item(0).getFirstChild().getNodeValue());
        
        // add bids
        NodeList bids = item.getElementsByTagName("Bid");
        for( int i = 0; i < bids.getLength(); ++i) {
           Bid b = parseBid((Element) bids.item(i));
           ib.addBid(b);
        } 
        // add location
        NodeList locations = item.getElementsByTagName("Location");
        Element location = (Element) locations.item(locations.getLength()-1);
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
        Element country = (Element) item.getElementsByTagName("Country").item(0);
        ib.setCountry(country.getFirstChild().getNodeValue());
        // add started
        ib.setStarted(item.getElementsByTagName("Started").item(0).getFirstChild().getNodeValue());
        // add Ends
        ib.setEnds(item.getElementsByTagName("Ends").item(0).getFirstChild().getNodeValue());
        // add seller rating and id
        Element seller = (Element) item.getElementsByTagName("Seller").item(0);
        ib.setSellerRating(seller.getAttribute("Rating"));
        ib.setSellerId(seller.getAttribute("UserID"));
        // add description
        ib.setDescription(item.getElementsByTagName("Description").item(0).getFirstChild().getNodeValue());
        
        // add buy price
        NodeList buyPrice = item.getElementsByTagName("Buy_Price");
        if (buyPrice.getLength() != 0) {
            ib.setBuyPrice(buyPrice.item(0).getFirstChild().getNodeValue());
        }
        } catch(Exception e) {
            StackTraceElement[] st = e.getStackTrace();
            String s = "";
            for (StackTraceElement el: st) {
              String temp = el.toString() +  "\n";
              s += temp;
            }
            ib.setId(s);
            return ib;
        }

        return ib;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	String itemId = request.getParameter("id");
	String itemXml = AuctionSearchClient.getXMLDataForItemId(itemId);
	if (itemXml.equals("")) { 
        RequestDispatcher rd = request.getRequestDispatcher("./getItem.html");
        rd.forward(request,response);
        return;
    }

   ItemBean ib = getItemBeanFromXml(itemXml);
	 request.setAttribute("info",ib);
    RequestDispatcher rd = request.getRequestDispatcher("./itemInfo.jsp");
	rd.forward(request,response);
    }
}
