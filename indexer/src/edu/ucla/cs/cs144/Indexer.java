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
import org.apache.lucene.document.StoredField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    Connection conn;
    IndexWriter indexWriter;
    

    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    /* directory to store the index data files */

    private void createIndexWriter() throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/item_index"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
            indexWriter.deleteAll();
            indexWriter.commit();
        }
    }
    
    /** 
       Writes a single item to the index
       @param item_id item id for the item to instert 
       @param name name of the item
       @param item_text any other searchable text(description, categories, etc) 
       */
    private void addItemToIndex(String item_id, String item_name, String item_text) throws IOException {
        Document item_doc = new Document();
        // Item id will be a purely stored field since we dont want to index on it
        item_doc.add(new StringField("item_id", item_id, Field.Store.YES));

        // add name as a seperate stored field because we want to retrieve it
        item_doc.add(new StringField("item_name", item_name, Field.Store.YES));

        /* add field for rest of indexable text(description + categories)
           don't store this field since we aren't interested in returning it
        */
        item_doc.add(new TextField("item_text", item_text, Field.Store.NO));
        
        // Write the new item document to the index
        indexWriter.addDocument(item_doc);
    }

    /**
        Populates the index using item data from the database
     */
    private void populateIndex() {
        DbSearcher s = new DbSearcher(conn);
        try {
            // pull in categories from database 
            s.mapCategories();

            // retrieve all items from database
            ResultSet items = s.getItems();
      
            // add every item to the index
            while( items.next() ){ 
                String i_id = items.getString("item_id");
                String name = items.getString("name");
                String description = items.getString("description");
                
                // get categories associated with item
                String cats = s.getCategoriesById(i_id);
                 
                //add item to index with name, description and categories indexable
                addItemToIndex(i_id,name,description + " " + cats + " " + name);
            }
            
            // close resultset and statement
            items.close(); 
            s.closeItemStatement();
        }
        catch (SQLException|IOException e) {
            System.out.println("Failed to populate index! " + e.getMessage());
            System.exit(1);
        }
    }

    /**
        Closes the previously opened indexWriter 
    */
    private void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    /**
        Rebuilds the index 
    */
    public void rebuildIndexes() {

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
	 */ 
	 
        try {
            //Creates new index and initializes its writer object
            createIndexWriter();

            //Interface with the mysql database to populate the index
            populateIndex();

            //Close index writer 
            closeIndexWriter();
        }
        catch (IOException e) {
            System.out.println("IOError: " + e.getMessage());
            System.exit(1);
        }
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
