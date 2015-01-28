/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    /* Hold current rows. not necessarily written to load files*/
    HashMap<Integer, String> userRows;
    static ArrayList<String> itemRows = new ArrayList<String>();
    static ArrayList<String> bidRows = new ArrayList<String>();
    static ArrayList<String> itemCategoryRows = new ArrayList<String>();

    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
   
    /* Names of the output load files */
    static final String ItemLFName = "./Item.load";
    static final String UserLFName = "./User.load";
    static final String BidLFName = "./Bid.load";
    static final String ItemCategoryLFName = "./ItemCategory.load";
   
    /* File objects for the output load files */ 
    BufferedWriter ItemLF;
    BufferedWriter UserLF;
    BufferedWriter BidLF;
    BufferedWriter ItemCategoryLF;
     
    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    /* Opens the output mysql load files for the 4 tables */
    //static void initLoadFiles(); 

    /* Dumps rows to the User load file.  
     * User(UID, srat, brat, long, lat, country)
     */
    //static int updateUserLF();
    
    /* Dumps row to the ItemCategory load file
     * ItemCategory(IID, Category)
     */
    //static int updateItemCategoryLF();
   
    /* Dumps rows to the Bids load file
     * Bids(UID,IID,time, amt)
     */ 
    //static int updateBidLF();
    
    /* Dumps rows to Item load file
     * Item(IID,name,bPrice,sPrice,numBids,long,lat,country,start,end,SellerID)
     */
    //static int updateItemLF();
   
    static String addCol(String row, String newCol) {
      return row + columnSeparator + newCol;       
    } 

    /* Parses an item node
     * Adds row(s) to itemRows,itemCategoryRows,bidRows,userRows  
     */
    static void parseItem(Element e){
       String itemID = e.getAttribute("ItemID");

       //Start row string for the item table
       String row= addCol(itemID, getElementTextByTagNameNR(e, "Name"));
        
       String buy_price = strip(getElementTextByTagNameNR(e, "Buy_Price"));
       if(buy_price == "") {
          row=addCol(row, "NULL"); //What are we using for null? \N?
       } else{
             row=addCol(row, buy_price);
       }

       row=addCol(row, strip(getElementTextByTagNameNR(e, "First_Bid")));
       row=addCol(row, getElementTextByTagNameNR(e, "Number_of_Bids"));
       
       Element loc = getElementByTagNameNR(e, "Location");
       //Latitude and Longitude are Location attributes
       String lat = loc.getAttribute("Latitude");
       if(lat.equals(null) || lat.equals("")) {
          row=addCol(row, "NULL");
       } else {
          row=addCol(row, lat);
       }
       String lon = loc.getAttribute("Longitude");
       if(lon.equals(null) || lon.equals("")) {
          row=addCol(row, "NULL");
       } else {
          row=addCol(row, lon);
       }
       row=addCol(row, getElementText(loc));

       row=addCol(row, getElementTextByTagNameNR(e, "Country")); 
       row=addCol(row, getElementTextByTagNameNR(e, "Started"));
       row=addCol(row, getElementTextByTagNameNR(e, "Ends"));

       //UserID is Seller attribute
       row=addCol(row, getElementByTagNameNR(e, "Seller").getAttribute("UserID"));

       row=addCol(row, getElementTextByTagNameNR(e, "Description"));

       itemRows.add(row);

       //Add each category of this item into the Category table 
       for ( Element curCat : getElementsByTagNameNR(e, "Category")) {
           itemCategoryRows.add(addCol(itemID, getElementText(curCat)));
       }
       
       Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(e, "Bids"), "Bid");
       if(bids.length != 0) {
          for(Element curBid : bids) {
             Element bidder=getElementByTagNameNR(curBid, "Bidder");
             String bidRow = addCol(bidder.getAttribute("UserID"), itemID);
             bidRow = addCol(bidRow, getElementTextByTagNameNR(curBid, "Time"));
             bidRows.add(addCol(bidRow, strip(getElementTextByTagNameNR(curBid, "Amount"))));
          }
       }

 
   }

    /* Parses a bid node 
     * Adds row(s) to bidRows and userRows
     */
    //static void parseBid(Element e);

    /* Parses a bidder node 
     * Adds row(s) to userRows
     */
    //static void parseBidder(Element e);

    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        /* Handle each item */
        for ( Element curItem : getElementsByTagNameNR(doc.getDocumentElement(), "Item")) {
            parseItem(curItem);
        } 
        /*for(String r :itemRows) {
          System.out.println(r);
        } 
        for(String b :bidRows) {
          System.out.println(b);
        }
        */
        for(String c : itemCategoryRows) {
          System.out.println(c);
        }
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
