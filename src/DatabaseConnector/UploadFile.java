package DatabaseConnector;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;

public class UploadFile {

    /**
     * Given the filepath, that file is then uploaded to the server and the filepath on the server
     * is returned
     * @param filepath path to the file that is being uploaded
     * @return file path of the file on the server to be stored on the database
     * @throws FileNotFoundException If the file at the filepath is not found
     */
    public static String uploadFile(String filepath) throws FileNotFoundException, HTTPErrorException {
        try {
            String url = "http://www2.macs.hw.ac.uk/~db47/WheelyGoodBikes/DatabaseLayer/create/uploadImage.php";
            String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
            String param1 = "value1";

            String query = String.format("fileToUpload=%s",
                    URLEncoder.encode(param1, charset));

            String param = "value";
            File imageFile = new File(filepath);
            if(imageFile == null) {
                throw new FileNotFoundException("Could not find file with the path " + filepath);
            }

            String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
            String CRLF = "\r\n"; // Line separator required by multipart/form-data.
            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            /**
            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(param).append(CRLF).flush();
**/
            // Send text file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"" + imageFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: image/jpeg;").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(imageFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();

            int status = ((HttpURLConnection)connection).getResponseCode();
            System.out.println("URL Code: " + status);

            if(status == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = br.read()) != -1) {
                    sb.append((char) cp);
                }
                br.close();
                return sb.toString();
            } else {
                throw new HTTPErrorException("Error code: " + status);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("Could not upload the file " + filepath);
        }
    }
}
