import DatabaseConnector.HTTPConnection;

public class HTTPRequest {

    public static void main(String[] args) {
        //send a request for all the getLocations
        System.out.println("------- Test Read Query ------ \n");
        String url = "http://www2.macs.hw.ac.uk/~db47/WheelyGoodBikes/DatabaseLayer/read/fetchLocations.php";
        String parameters = ""; //no parameters

        sendRequest(url, parameters);

        System.out.println("------- Test Insert Query ------ \n");
        url = "http://www2.macs.hw.ac.uk/~db47/WheelyGoodBikes/DatabaseLayer/create/addLocation.php";
        parameters = "location_name=Glasgow"; //no parameters

        sendRequest(url, parameters);

    }

    private static void sendRequest(String url, String parameters) {
        HTTPConnection con = new HTTPConnection();

        String result = "";



        try {
            result = con.getResponse(url, parameters);
        } catch (Exception e) {
            System.err.println("Failed. \n");
            e.printStackTrace();
        }

        if(result.equals("")) {
            //failure, we expect a response
            System.err.println("Failed. \n");
        } else {
            System.out.print(result);
        }
    }


}
