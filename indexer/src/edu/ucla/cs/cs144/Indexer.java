package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.util.*;

public class Indexer {
    Connection conn;
    IndexWriter indexWriter;
    static HashMap<String, ArrayList<String>> catMap = new HashMap<String, ArrayList<String>>();

    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    /* directory to store the index data files */
    //private static final indexDir = "index_data";

    private void createIndexWriter() throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("index-directory"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
    }

    private void populateIndex() {
    
    }

    /* Closes the indexWriter */
    private void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    public void rebuildIndexes() throws SQLException{

        conn = null;

        // create a connection to the database to retrieve Items from MySQL
	   try {
	       conn = DbManager.getConnection(true);
	   } catch (SQLException ex) {
	       System.out.println(ex);
	   }

	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */ try {
            String i_id, cat, name, description;
            
            //Get all categories
            Statement cat_s = conn.createStatement();
            ResultSet cat_rs = cat_s.executeQuery("SELECT * FROM ItemCategory");

            while (cat_rs.next()) {
                i_id = cat_rs.getString("item_id");
                cat = cat_rs.getString("category");

                //If no categories have been mapped for this item_id yet
                if(catMap.get(i_id) == null) {
                    ArrayList<String> catList = new ArrayList<String>();
                    catList.add(cat);
                    catMap.put(i_id, catList);
                } else {
                    ArrayList<String> newCatList = catMap.get(i_id);
                    newCatList.add(cat);
                    catMap.put(i_id, newCatList);
                }
            }
            cat_rs.close();
            cat_s.close();

            //Get all items 
            Statement s = conn.createStatement() ;
            ResultSet rs = s.executeQuery("SELECT item_id, name, description FROM Item");
            //For every item
            while( rs.next() ){ 
                i_id = rs.getString("item_id");
                name = rs.getString("name");
                description= rs.getString("description");

                //Create string of all of this current item's categories
                StringBuilder catBuilder = new StringBuilder();
                ArrayList<String> cats = catMap.get(i_id);
                for(String curCat : cats) {
                    catBuilder.append(curCat);
                    catBuilder.append(" ");
                }
                String itemCats = catBuilder.toString();

                //Insert code here to add index with name, category, description
            }

            //close the resultset, statement and connection
            rs.close();
            s.close();

            conn.close();
        } /*catch (ClassNotFoundException ex){
            System.out.println(ex);
        }*/ catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        }

/*
        try {
            //Creates new index and initializes its writer object
            createIndexWriter();

            //Interface with the mysql database to populate the index
            fillIndex();

            //Close index writer 
            closeIndexWriter();
        }
        catch (IOException e) {
            System.out.println("IOError: " + e.getMessage());
            System.exit(1);
        }
*/
        // close the database connection
	   try {
	       conn.close();
	   } catch (SQLException ex) {
	       System.out.println(ex);
	   }
    }    

    public static void main(String args[]) throws SQLException{ 
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
