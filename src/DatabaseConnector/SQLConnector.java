package DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLConnector {

    private Connection con;
    private Logger lgr = Logger.getLogger(SQLConnector.class.getName());

    /**
     * @return boolean - if the SQL connection has been established
     */
    public boolean connect() {
        lgr.log(Level.INFO, "Connecting to database with predefined credentials...");
        //Set up SSH Tunnel
       // SSHTunnel tunnel = new SSHTunnel();
        //tunnel.connect();
        //int port = tunnel.getInternalPort();

        //set up SSH information
        String url = "jdbc:mysql://localhost:3306/project?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "dale";
        String password = "mysqlprojects";

        String query = "SELECT VERSION()";


        try {
            con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()) {
                lgr.log(Level.INFO, "Connection to database established");
            }
            return true;
        } catch (SQLException e) {
            lgr.log(Level.SEVERE, e.getMessage(), e);
           // tunnel.disconnect();
            return false;
        }
    }

    /**
     * @return boolean - if the SQL Connection has been disconnected
     */
    public boolean disconnect() {
        if(con == null) {
            lgr.log(Level.WARNING, "Disconnect attempted while no active MySQL connection exists. Aborting");
            return false;
        }
        try {
            con.close();
            return true;
        } catch (SQLException e) {
            lgr.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public Connection getConnectionObject() {
        return this.con;
    }
}
