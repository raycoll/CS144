package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

class DbSearcher {
    Connection conn; 

    DbSearcher(Connection c) {
        conn = c;
    }

    /*
    ResultSet getItems() throws SQLException;
    ResultSet getCategories() throws SQLException;
    String getCategoriesById(int item_id) throws SQLException;
    */
}
