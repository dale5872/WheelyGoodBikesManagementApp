import DatabaseConnector.Query;

public class QueryRequest {

    public static void main(String[] args) {
        //send a request for all the getLocations
        System.out.println("------- Test Read Query ------ \n");
        String parameters = ""; //no parameters

        Query q = new Query();
        q.executeQuery("read", "fetchLocations", parameters);

        //Expected to print the json response for a successful query

    }


}
