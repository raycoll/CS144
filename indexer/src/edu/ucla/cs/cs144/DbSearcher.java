package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DbSearcher {
    Connection conn; 
    static HashMap<Integer, ArrayList<String>> catMap = new HashMap<Integer, ArrayList<String>>();
    Statement item_s;

    DbSearcher(Connection c) {
        conn = c;
    }

    
    ResultSet getItems() throws SQLException {
    	item_s = conn.createStatement() ;
        return item_s.executeQuery("SELECT item_id, name, description FROM Item");
    }

    void mapCategories() throws SQLException {
    	Statement cat_s = conn.createStatement();
        ResultSet cat_rs= cat_s.executeQuery("SELECT * FROM ItemCategory");

        while (cat_rs.next()) {
            int i_id = cat_rs.getInt("item_id");
            String cat = cat_rs.getString("category");

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
    }

    String getCategoriesById(int item_id) throws SQLException {
    	//Create string of all of this current item's categories
        StringBuilder catBuilder = new StringBuilder();
        ArrayList<String> cats = catMap.get(item_id);
        for(String curCat : cats) {
            catBuilder.append(curCat);
            catBuilder.append(" ");
        }
        return catBuilder.toString();
    }
    
    void closeItemStatement() throws SQLException {
        item_s.close();
    }
}
