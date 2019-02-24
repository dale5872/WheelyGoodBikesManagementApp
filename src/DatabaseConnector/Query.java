package DatabaseConnector;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.HttpURLConnection;

public class Query {

    private String type;
    private String scriptName;
    private String args;
    private Logger lgr = Logger.getLogger(Query.class.getName());

    /**
     * @param type type of SQL query to execute (read, update, create, delete)
     * @param scriptName the name of the PHP script (without the .php extension)
     * @param args any POST args in the form of "arg1=value1&arg2=value2"
     */
    public Query(String type, String scriptName, String args) {
        this.type = type;
        this.scriptName = scriptName;
        this.args = args;
    }

    public Query() {}

    /**
     * @param  - the PHP query filename to execute
     * @param args - the arguments to send IN ORDER AS THEY APPEAR IN THE PHP SCRIPT
     * @return - the ResultSet object containing the results from the executed Query
     * Uses Connection object from SQLConnector to connect to and execute the
     * Query on the database
     */
    public Results executeQuery(String type, String scriptName, String args) {
        this.type = type;
        this.scriptName = scriptName;
        this.args = args;

        return executeQuery();
    }

    public Results executeQuery() {
        //Log
        lgr.log(Level.INFO, "Executing " + this.type + " query on " + this.scriptName + " with args " + this.args);
        try {
            //Set HTTP POST data
            String url = "http://www2.macs.hw.ac.uk/~db47/WheelyGoodBikes/DatabaseLayer/" + this.type + "/" + this.scriptName + ".php";
            HTTPConnection con = new HTTPConnection();

            //Get response
            String response = con.getResponse(url, this.args);

            //Parse the data into the Results object and return
            Results res = JSONData.getResults(JSONData.parseData(response));

            //log
            lgr.log(Level.INFO, "Query successful");
            return res;
        } catch (IOException exc) {
            lgr.log(Level.SEVERE, "IOException Occured");
            exc.printStackTrace();
        } catch (HTTPErrorException exc) {
            //prints error
        } catch (JSONErrorException exc) {
            //prints error
        } catch (TimeoutException exc) {
            //prints error
        }
        lgr.log(Level.SEVERE, "Query failed");
        return null;
    }

    public boolean insertQuery(String type, String scriptName, String args) {
        this.type = type;
        this.scriptName = scriptName;
        this.args = args;
        return insertQuery();
    }

    public boolean insertQuery() {
        try {
            //Set HTTP POST data
            String url = "http://www2.macs.hw.ac.uk/~db47/WheelyGoodBikes/DatabaseLayer/" + this.type + "/" + this.scriptName + ".php";
            HTTPConnection con = new HTTPConnection();

            //Get response
            String response = con.getResponse(url, this.args);

            //Parse JSON to get success / fail
            return JSONData.checkSucceeded(JSONData.getJSONObject(response));
        } catch (IOException exc) {
            lgr.log(Level.SEVERE, "IOException Occured");
            exc.printStackTrace();
        } catch (HTTPErrorException exc) {
            //prints error
            return false;
        } catch (JSONErrorException exc) {
            //prints error
            return false;
        } catch (TimeoutException exc) {
            //prints error
            return false;
        }

        //if we get here, we can be sure that it failed
        return false;

    }
    /**
     *
     */
    public void updateQuery(String type, String scriptName, String args) {
        this.type = type;
        this.scriptName = scriptName;
        this.args = args;
    }

}
