package DatabaseConnector;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.HttpURLConnection;

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
     * @param query - the PHP query filename to execute
     * @param args - the arguments to send IN ORDER AS THEY APPEAR IN THE PHP SCRIPT
     * @return - the ResultSet object containing the results from the executed Query
     * Uses Connection object from SQLConnector to connect to and execute the
     * Query on the database
     *
     * TODO Replace JDBC with PHP
     * BODY Switching to PHP DB connectivity and removing JDBC, results will be parsed in JSON
     */
    public Results executeQuery(String type, String scriptName, String args) {
        String url = "http://www2.macs.hw.ac.uk/~db47/WheelyGoodBikes/DatabaseLayer/" + type + "/" + scriptName + ".php";
        HTTPConnection con = new HTTPConnection();

        try {
            String response = con.getResponse(url, args);

            System.out.println(response); //TEST
            /**
             * TODO Parse response
             * BODY Parse the JSON response and input data into the Results Object
             */

            /**
             * TODO Catch Exceptions
             * BODY Catch exceptions, log them and display to user. Find out why IOException is thrown
             */
        } catch (IOException exc) {
            lgr.log(Level.SEVERE, "IOException Occured");
            exc.printStackTrace();
        } catch (HTTPErrorException exc) {
            //prints error
        }
        return null;
    }

    public Results executeQuery() { return null; }
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
        return false;
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

    private void disconnectDB () {
        //Disconnect and log
        sqlCon.disconnect();
        lgr.log(Level.INFO, ("Query executed, SQL Connection closed."));
    }
}
