package DatabaseConnector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPConnection {

    /**
     * @param url - url to connect to
     * @return HttpURLConnection object to connect to the host
     * @throws IOException
     */
    public HttpURLConnection getConnection(String url) throws IOException {
        //set up objects
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        //add request headers
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "WheelyGoodBikes/1.0");

        return con;
    }

    /**
     * @param url - url to connect to
     * @param parameterString - string to send post parameters
     * @return the output message from the remote PHP script
     * @throws IOException
     * @throws HTTPErrorException if HTTP response code is not 200 as expected
     */
    public String getResponse(String url, String parameterString) throws IOException, HTTPErrorException {
        HttpURLConnection con = getConnection(url);

        //send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameterString);
        wr.flush();
        wr.close();

        //get response
        int responseCode = con.getResponseCode();
        if(responseCode != 200) {
            throw new HTTPErrorException("HTTP Error code: " + responseCode);
        }

        BufferedReader input = new BufferedReader((new InputStreamReader(con.getInputStream())));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();

        return response.toString();
    }
}
