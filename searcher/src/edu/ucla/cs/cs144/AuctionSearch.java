package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;


public class AuctionSearch implements IAuctionSearch {
    IndexSearcher searcher;
    QueryParser parser;

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */


    /** Creates a new instance of SearchEngine */
    public AuctionSearch() {
        try {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/item_index"))));
        
        
        parser = new QueryParser("item_text", new StandardAnalyzer());
        }
        catch (IOException e) {
            System.out.println("failed to open index or parser! " + e.getMessage());
            System.exit(1);
        }
    }	

    public int getResultLen(TopDocs t, int numResultsToSkip, int numResultsToReturn) {
            // set size of output array depending number of results returned
            // if they are more than numResultsToReturn, set size to be 
            // numResultsToReturn, otherwise use the smaller length 
            if ((t.scoreDocs.length - numResultsToSkip) > numResultsToReturn) {
                return numResultsToReturn;
            }
            return t.scoreDocs.length - numResultsToSkip;
    }

	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
        SearchResult[] res = null;
        
        // return null if asked to skip all or more returned
        if (numResultsToSkip >= numResultsToReturn) {
            return new SearchResult[0];
        }
        
        try {
            // parse query 
            Query q = parser.parse(query); 
            
            // perform search
            TopDocs t = searcher.search(q, numResultsToReturn);
            
            // return null if skipping more results than returned
            if (t.scoreDocs.length <= numResultsToSkip) {
                return new SearchResult[0];
            }

            int resultLen = getResultLen(t, numResultsToSkip, numResultsToReturn);
            
            // populate results array
            res = new SearchResult[resultLen];
            ScoreDoc[] resultScores = t.scoreDocs;
            for(int i = numResultsToSkip; i < resultLen; i++) {
                Document doc = searcher.doc(resultScores[i].doc);
                res[i] = new SearchResult(doc.get("item_id"),doc.get("item_name"));    
            }
            
        }
        catch (ParseException|IOException e) {
            System.out.println("query search failed! " + e.getMessage());
            System.exit(1);
        }

        return res;
	}

    /** Returns a list of item_ids from the mysql database 
      * that are in the given region 
      */
    private SearchResult[] getItemsInRegion(SearchRegion region){
        Statement s;
        Connection conn;
        ResultSet rs;
        int size;
        SearchResult[] res= new SearchResult[0];
        ArrayList<SearchResult> sr= new ArrayList<SearchResult>();
        try{

            conn = DbManager.getConnection(true);
            s = conn.createStatement();

            double minx = region.getLx();
            double miny = region.getLy();
            double maxx = region.getRx();
            double maxy = region.getRy();

            rs = s.executeQuery("SELECT item_id FROM Location WHERE MBRCONTAINS(GeomFromText(\'Polygon(("+minx+" "+miny+", "+maxx+" "+miny
            +", "+maxx+" "+maxy+", "+minx+" "+maxy+", "+minx+" "+miny+"))\'), g)=1");

            while(rs.next()) {
                SearchResult currsr = new SearchResult();
                currsr.setItemId(rs.getString("item_id"));
                sr.add(currsr);
            }
            rs.close();
            s.close();
            conn.close();

        }catch (SQLException e) {
            System.out.println("ERROR: SQLException "+e.getMessage());
            System.exit(1);
        }
        return sr.toArray(new SearchResult[sr.size()]);
    }
        
	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
        // Perform spatial query 
        SearchResult[] spatialResults = getItemsInRegion(region);
        if (spatialResults.length == 0) {
            return new SearchResult[0];
        }

        // Perform basic search
        SearchResult[] queryResults = basicSearch(query,0, 9999); 
        if (queryResults.length == 0) {
            return new SearchResult[0];
        }

        // Get intersection
        SearchResult[] intersection = new SearchResult[9999];
        int intersection_size = 0; 
        for(int i = 0; i < queryResults.length; i++) { 
            for (int j = 0; j < spatialResults.length; j++) {
                if (queryResults[i].getItemId().equals(spatialResults[j].getItemId())) {
                    intersection[intersection_size] = queryResults[i];
                    intersection_size++;
                }    
           	}
        }

        // Create output array
        int output_size = 0;
        if (intersection_size <= numResultsToSkip) {
            return new SearchResult[0];
        }
        else if (intersection_size < numResultsToSkip + numResultsToReturn) {
            output_size = intersection_size - numResultsToSkip; 
        }
        else { // intersection_size >= numResultsToSkip + numResultsToReturn
            output_size = numResultsToReturn;
        }
        SearchResult[] output = new SearchResult[output_size];

        // Fill output array
        for (int i = 0; i < output_size; i++) {
            output[i] = intersection[i + numResultsToSkip];
        }

        return output;
	}
    public String escXMLChar(String s) {
        return s.replace("\\", "\\\\").replace("\"","\\\"").replace("&","&amp;").replace("\'", "&apos;").replace("<","&lt;")
        .replace(">","&gt;");
    }

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
        //String ret="";
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat xmlFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

        try {
            Connection conn = DbManager.getConnection(true);

            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Item WHERE item_id ="+itemId);

            while(rs.next()) {
                sb.append("<Item ItemID=\"").append(rs.getString("item_id"));
                sb.append("\">\n<Name>").append(escXMLChar(rs.getString("name"))).append("</Name>\n");

                Statement cat_s = conn.createStatement();
                ResultSet cat_rs = cat_s.executeQuery("SELECT category FROM ItemCategory WHERE item_id="+itemId);
                while(cat_rs.next()) {
                    sb.append("<Category>").append(escXMLChar(cat_rs.getString("category"))).append("</Category>\n");
                }

                cat_rs.close();
                cat_s.close();

                Statement bid_s = conn.createStatement();
                ResultSet max_rs = bid_s.executeQuery("SELECT MAX(amount) as currently FROM Bid WHERE item_id="+itemId);

                while(max_rs.next()){
                    sb.append("<Currently>$");
                    if(max_rs.getString("currently")==null) {
                        sb.append(rs.getString("start_price"));
                    } else {
                        sb.append(max_rs.getString("currently"));
                    }
                    sb.append("</Currently>\n");
                }
                max_rs.close();

                if(rs.getString("buyprice")!=null) {
                    sb.append("<Buy_Price>$").append(rs.getString("buyprice")).append("</Buy_Price>\n");
                }
                sb.append("<First_Bid>$").append(rs.getString("start_price")).append("</First_Bid>\n");
                

                String num_bids = rs.getString("num_bids");
                sb.append("<Number_of_Bids>").append(num_bids).append("</Number_of_Bids>\n");

                if(num_bids.equals("0")) { //No bids so just attach one tag
                    sb.append("<Bids />\n");
                } else { //NEED TO TEST BID XML DATA
                    sb.append("<Bids>\n");

                    ResultSet bid_rs = bid_s.executeQuery("SELECT * FROM Bid WHERE item_id="+itemId);
                    Statement usr_s = conn.createStatement();
                    while(bid_rs.next()){
                    
                        String bidder = bid_rs.getString("user_id");
                        sb.append("<Bid>\n");

                        ResultSet usr_rs = usr_s.executeQuery("SELECT * FROM AuctionUser WHERE user_id=\'"+bidder+"\'");
                        if(usr_rs.next()) { 
                            sb.append("<Bidder Rating=\"").append(usr_rs.getString("buy_rating")).append("\" User=\"")
                                .append(bidder).append("\">\n");
                            sb.append("<Location>").append(escXMLChar(usr_rs.getString("location"))).append("</Location>\n");
                            sb.append("<Country>").append(escXMLChar(usr_rs.getString("country"))).append("</Country>\n");

                            sb.append("</Bidder>\n");
                        }
                        usr_rs.close();
                        try{
                            Date bidTime = sqlFormat.parse(bid_rs.getString("time"));
                            sb.append("<Time>").append(xmlFormat.format(bidTime)).append("</Time>\n");
                        } catch (java.text.ParseException e){
                            System.out.println("ERROR: Cannot parse date");
                        }
                            

                        sb.append("<Amount>$").append(bid_rs.getString("amount")).append("</Amount>\n")
                            .append("</Bid>\n");
                    }
                    sb.append("</Bids>\n");

                    bid_rs.close();
                    usr_s.close();
                    bid_s.close();
                }
                


                            
                if(rs.getString("latitude")==null && rs.getString("longitude")==null) {
                    sb.append("<Location>");
                } else{
                    sb.append("<Location Latitude=\"").append(rs.getString("latitude")).append("\" Longitude=\"")
                        .append(rs.getString("longitude")).append("\">\n");
                }

                sb.append(escXMLChar(rs.getString("location"))).append("</Location>\n");
                sb.append("<Country>").append(escXMLChar(rs.getString("country"))).append("</Country>\n");

                try{             
                    Date started = sqlFormat.parse(rs.getString("started"));
                    Date ends = sqlFormat.parse(rs.getString("ends"));
                    sb.append("<Started>").append(xmlFormat.format(started)).append("</Started>\n");
                    sb.append("<Ends>").append(xmlFormat.format(ends)).append("</Ends>\n");
                } catch (java.text.ParseException e){
                    System.out.println("ERROR: Cannot parse date");
                }

                
                String seller = rs.getString("seller_id");
                Statement usr2_s = conn.createStatement();

                
                ResultSet slr_rs = usr2_s.executeQuery("SELECT * FROM AuctionUser WHERE user_id=\'"+seller+"\'");
                if(slr_rs.next()) { 
                    sb.append("<Seller Rating=\"").append(slr_rs.getString("sell_rating")).append("\" UserID=\"")
                        .append(seller).append("\" />\n");
                }
                
                slr_rs.close();
                usr2_s.close();

                if(rs.getString("description").equals("")){
                     sb.append("<Description />\n");
                } else {
                    sb.append("<Description>").append(escXMLChar(rs.getString("description"))).append("</Description>\n");
                }
                sb.append("</Item>");
                

            }//end while(rs.next())
        } catch (SQLException e) {
            System.out.println("ERROR: SQLException "+e.getMessage());
            System.exit(1);
        } 

	   return  sb.toString();
	}
	
	public String echo(String message) {
		return message;
	}

}
