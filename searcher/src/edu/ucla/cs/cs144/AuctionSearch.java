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
import org.apache.lucene.queryparser.classic.ParseException;
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
            return null;
        }
        
        try {
            // parse query 
            Query q = parser.parse(query); 
            
            // perform search
            TopDocs t = searcher.search(q, numResultsToReturn);
            
            // return null if skipping more results than returned
            if (t.scoreDocs.length <= numResultsToSkip) {
                return null;
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
        SearchResult[] res=null;
      try{
        conn = DbManager.getConnection(true);
        s = conn.createStatement();

        double minx = region.getLx();
        double miny = region.getLy();
        double maxx = region.getRx();
        double maxy = region.getRy();

        rs = s.executeQuery("SELECT item_id FROM Location WHERE MBRCONTAINS(GeomFromText(\'Polygon(("+minx+" "+miny+", "+maxx+" "+miny
            +", "+maxx+" "+maxy+", "+minx+" "+maxy+", "+minx+" "+miny+"))\'), g) =1;");
        size =rs.getFetchSize();

        res = new SearchResult[size];
        int i =0;
        while(rs.next()) {
            res[i]=new SearchResult();
            res[i].setItemId(rs.getString("item_id"));
            i++;
        }
      }catch (SQLException e) {
                System.out.println("ERROR: SQLException "+e.getMessage());
                System.exit(1);
      }
      return res;
      

    }
        
	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
        // Perform spatial query 
        SearchResult[] spatialResults = getItemsInRegion(region);
        if (spatialResults == null) {
            return null;
        }

        // Perform basic search
        SearchResult[] queryResults = basicSearch(query,0, 9999); 
        if (queryResults == null) {
            return null;
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
            return null;
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

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
       ///DbManager db = new DbManager();
       /*
        Connection conn = DbManager.getConnection(true);
        Statement s = conn.createStatement();

        ResultSet rs = s.executeQuery("SELECT * FROM Item WHERE item_id ="+itemId);

        StringBuilder sb = new StringBuilder();
*/

		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
