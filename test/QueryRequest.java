import DatabaseConnector.Query;

public class QueryRequest {

    public static void main(String[] args) {
        //send a request for all the locations
        System.out.println("------- Test Read Query ------ \n");
        String parameters = "location_name=Glasgow"; //no parameters

        Query q = new Query();
        q.executeQuery("create", "addLocation", parameters);

        //Expected to print the json response for a successful query

    }


}
