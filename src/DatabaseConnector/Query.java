//TODO: Replace JDBC with PHP
package DatabaseConnector;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Query {

    private String query;
    private Logger lgr = Logger.getLogger(Query.class.getName());
    private SQLConnector sqlCon;

    /**
     * @param query - SQL Query to be executed
     */
    public Query(String query) {
        this.query = query;
    }

    public Query() {}

    /**
     * @return - the ResultSet object containing the results from the executed Query
     * Uses Connection object from SQLConnector to connect to adn execute the
     * Query on the database
     */
    public Results executeQuery() {
        Connection con = connectToDB();

        try{
            //Create SQL Statement, create ResultSet object and fill that object
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(this.query);
            ResultSetMetaData rsma = rs.getMetaData();

            int cols = rsma.getColumnCount();
            int rows = 0;
            //find the number of rows by iterating through the ResultSet
            while(rs.next()) {
                rows++;
            }
            rs.beforeFirst();

            //Get column names
            String[] columnNames = new String[cols];

            for(int i = 1; i < cols; i++) {
                columnNames[i] = rsma.getColumnName(i);
            }

            //As any ResultSet object is destroyed when used outside of statement,
            //data must be moved to a Results object
            Results results = new Results(rows, cols);
            results.setColumn(columnNames);

            //Import the data
            int r = 0;
            while(rs.next()) {
                for(int c = 1; c < cols+1; c++) {
                    results.setElement(r, c-1, rs.getObject(c));
                }
                r++;
            }

            return results;

        } catch (SQLException e) {
            lgr.log(Level.SEVERE, e.getMessage(), e);
            sqlCon.disconnect();
            return null;
        }

    }

    /**
     * @param query - new Query to be executed
     * @return ResultSet from the database
     * Saves updating the Query and then executing
     */
    public Results executeQuery(String query) {
        this.query = query;
        return executeQuery();
    }

    public boolean insertQuery(String query) {
        this.query = query;
        return insertQuery();
    }

    public boolean insertQuery() {
        Connection con = connectToDB();
        lgr.log(Level.INFO, "Executing insert query");

        try {
            Statement st = con.createStatement();
            int rowsAffected = st.executeUpdate(this.query);
            disconnectDB();

            if(rowsAffected > 0) {
                lgr.log(Level.INFO, "SQL INSERT statement executed successfully.");
                return true;
            } else {
                lgr.log(Level.SEVERE, "SQL INSERT statement executed unsuccessfully.");
                return false;
            }


        } catch (SQLException e) {
            disconnectDB();
            lgr.log(Level.SEVERE, e.getMessage(), e);
            sqlCon.disconnect();
            return false;
        }
    }

    /**
     * @param query - Query to be updated
     */
    public void updateQuery(String query) {
        this.query = query;
    }

    public Logger returnLogger() {
        return lgr;
    }

    private Connection connectToDB() {
        //Connect to the database and return the SQL Connection Object
        sqlCon = new SQLConnector();
        if(!sqlCon.connect()) {
            lgr.log(Level.SEVERE, "Connection to the database failed, aborting query execution");
            return null;
        }
        return sqlCon.getConnectionObject();
    }

    private void disconnectDB () {
        //Disconnect and log
        sqlCon.disconnect();
        lgr.log(Level.INFO, ("Query executed, SQL Connection closed."));
    }
}
