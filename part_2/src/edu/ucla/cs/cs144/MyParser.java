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
    static HashMap<String, ArrayList<String>> userRows = new HashMap<String, ArrayList<String>>();
    static ArrayList<String> itemRows = new ArrayList<String>();
    static ArrayList<String> bidRows = new ArrayList<String>();
    static ArrayList<String> itemCategoryRows = new ArrayList<String>();

    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
   
    /* Names of the output load files */
    static final String ItemLFName = "./Item.dat";
    static final String UserLFName = "./AuctionUser.dat";
    static final String BidLFName = "./Bid.dat";
    static final String ItemCategoryLFName = "./ItemCategory.dat";
   
    /* File objects for the output load files */ 
    static BufferedWriter ItemBW;
    static BufferedWriter UserBW;
    static BufferedWriter BidBW;
    static BufferedWriter ItemCategoryBW;
     
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
    static void initLoadFiles() {
         try {
         File UserLF = new File(UserLFName);
         File BidLF = new File(BidLFName);
         File ItemLF = new File(ItemLFName);
         File ItemCategoryLF = new File(ItemCategoryLFName);
           if (!UserLF.exists()) {
               UserLF.createNewFile();
           }
           if (!BidLF.exists()) {
               BidLF.createNewFile();
           }
           if (!ItemLF.exists()) {
               ItemLF.createNewFile();
           }
           if (!ItemCategoryLF.exists()) {
               ItemCategoryLF.createNewFile();
           }

         UserBW = new BufferedWriter(new FileWriter(UserLF));
          BidBW = new BufferedWriter(new FileWriter(BidLF));
          ItemBW = new BufferedWriter(new FileWriter(ItemLF));
          ItemCategoryBW = new BufferedWriter(new FileWriter(ItemCategoryLF));
         } 
         catch (Exception e) {
            System.out.println("Failed to create outfiles!");
            System.exit(1);
         }
    }

    static void closeLoadFiles() {
        try {
        UserBW.flush();
        UserBW.close();
        BidBW.flush();
        BidBW.close();
        ItemBW.flush();
        ItemBW.close();
        ItemCategoryBW.flush();
        ItemCategoryBW.flush();
        }
        catch (Exception e) {
            System.out.println("Failed to close outfiles!");
            System.exit(1);
       }
    }
    /* Dumps rows to the User load file.  
     * User(UID, srat, brat, long, lat, country)
     */
    static void updateUserLF() {
       for (ArrayList<String> value : userRows.values()) {
           for (String s : value) {
               try {
                   UserBW.write(s,0,s.length());
               }
               catch (Exception e) {
                   System.out.println("Failed to write to AuctionUser outfile!");
                   System.exit(1);
               }    
           }
           try { 
               UserBW.write('\n');
           }
           catch (Exception e) {
               System.out.println("Failed to write newline to user outfile!");
               System.exit(1);
           }
       }
    }
    
    /* Dumps row to the ItemCategory load file
     * ItemCategory(IID, Category)
     */
    static void updateItemCategoryLF() {
        for (String row : itemCategoryRows) {
               try {
                   UserBW.write(row,0,row.length());
                   UserBW.write('\n');
               }
               catch (Exception e) {
                   System.out.println("Failed to write to AuctionUser outfile!");
                   System.exit(1);
               }    
           }
       }  
   
    /* Dumps rows to the Bids load file
     * Bids(UID,IID,time, amt)
     */ 
    static void updateBidLF() {
        for (String row : bidRows) {
               try {
                   UserBW.write(row,0,row.length());
                   UserBW.write('\n');
               }
               catch (Exception e) {
                   System.out.println("Failed to write to Bid outfile!");
                   System.exit(1);
               }    
        }
    }  
     
    
    /* Dumps rows to Item load file
     * Item(IID,name,bPrice,sPrice,numBids,long,lat,country,start,end,SellerID)
     */
    static void updateItemLF() {
        for (String row : itemRows) {
               try {
                   UserBW.write(row,0,row.length());
                   UserBW.write('\n');
               }
               catch (Exception e) {
                   System.out.println("Failed to write to Item outfile!");
                   System.exit(1);
               }    
       }
    }  
   
    /* Returns previous row appended with a new column
    *  Separated by columnSeperator
    */
    static String addCol(String row, String newCol) {
      return row + columnSeparator + newCol;       
    } 

    /* Parses an item node
     * Adds row(s) to itemRows,itemCategoryRows,bidRows,userRows  
     */
    static void parseItem(Element e){

       String itemID = e.getAttribute("ItemID");

       //ITEMS ARRAYLIST
       //Start row string for the item table
       String row= addCol(itemID, getElementTextByTagNameNR(e, "Name"));
        
       String buy_price = strip(getElementTextByTagNameNR(e, "Buy_Price"));
       if(buy_price.equals("")) {
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
       Element seller = getElementByTagNameNR(e, "Seller");
       String sellerID = seller.getAttribute("UserID");
       row=addCol(row, sellerID);

       row=addCol(row, getElementTextByTagNameNR(e, "Description"));

       itemRows.add(row);


       //CATEGORY ARRAYLIST
       //Add each category paired with the current itemID
       for ( Element curCat : getElementsByTagNameNR(e, "Category")) {
           itemCategoryRows.add(addCol(itemID, getElementText(curCat)));
       }


      //AUCTIONUSER HASHMAP
      //Check seller's userID. If does not exist, add a new row.
      //If it is already in the HashMap, then add seller rating if it's not in there already
      if(userRows.get(sellerID) != null) {
          ArrayList<String> updateUser = userRows.get(sellerID);
          //Check if seller rating needs to be added
          if(updateUser.get(1).equals("NULL")){ 
            updateUser.set(1, seller.getAttribute("Rating"));
            userRows.put(sellerID, updateUser);
          }
          
      } else {
        ArrayList<String> userRow = new ArrayList<String>(5);
        userRow.add(sellerID);
        userRow.add(seller.getAttribute("Rating")); //Sell_Rating
        userRow.add("NULL"); //Buy_Rating
        userRow.add("NULL"); //Location
        userRow.add("NULL"); //Country
        userRows.put(sellerID, userRow);    
      }


       Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(e, "Bids"), "Bid");
       if(bids.length != 0) {
          for(Element curBid : bids) {

            //BID ARRAYLIST
            //Add User_ID, Item_ID, Time, Amount for each Bid element into bidRows
             Element bidder=getElementByTagNameNR(curBid, "Bidder");
             String bidderID = bidder.getAttribute("UserID");

             String bidRow = addCol(bidderID, itemID);
             bidRow = addCol(bidRow, getElementTextByTagNameNR(curBid, "Time"));
             bidRows.add(addCol(bidRow, strip(getElementTextByTagNameNR(curBid, "Amount"))));
             


             //AUCTIONUSER HASHMAP
             //Check if new user needs to be added or existing user needs to have bidder information added
             if(userRows.get(bidderID) != null) { //UserID has already been seen

                ArrayList<String> updateByr = userRows.get(bidderID);

                //If buyer rating is "NULL", that means buyer info hasn't been added
                if(updateByr.get(2).equals("NULL")){ 
                  updateByr.set(2, bidder.getAttribute("Rating"));
                  updateByr.set(3, getElementTextByTagNameNR(bidder, "Location"));
                  updateByr.set(4, getElementTextByTagNameNR(bidder, "Country"));
                  userRows.put(bidderID, updateByr);
                }
             } else { //New UserID, create a new "row" for this user (<key, value> mapping)
                ArrayList<String> userRow = new ArrayList<String>(5);
                userRow.add(bidderID);
                userRow.add("NULL"); //Sell_Rating
                userRow.add(bidder.getAttribute("Rating")); //Buy_Rating
                userRow.add(getElementTextByTagNameNR(bidder, "Location")); //Location
                userRow.add(getElementTextByTagNameNR(bidder, "Country")); //Country
                userRows.put(bidderID, userRow);   
             }
             
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

        /* Flush rows to output files */
        updateItemLF();
        updateItemCategoryLF();
        updateBidLF(); 

        /* Clear the in-memory data since we flushed to file */
        itemRows.clear();
        bidRows.clear();
        itemCategoryRows.clear();

        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize output files */
        initLoadFiles(); 

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

        /* Flush user rows to output file */
        updateUserLF();
        
        /* Close all output files(must do this since we used BufferedWriter) */
        closeLoadFiles();  
    }
}
