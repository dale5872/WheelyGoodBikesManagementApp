package DatabaseConnector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONData {

    /**
     * @param json json Text Data
     * @return JSONObject object
     * Returns a JSON Object that can parse any json
     */
    public static JSONObject getJSONObject (String json) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) parser.parse(json);
            return jsonObject;
        } catch (ParseException exc) {

        }
        return null;
    }

    /**
     * @param json json Text Data
     * @return JSON Array of the parsed JSON text
     *
     * TODO Finish implementation of the method
     * BODY Return a JSON Array of the data recieved from PHP Scripts
     */
    public static JSONArray getData (String json) {
        JSONObject obj = getJSONObject(json);

        return null;
    }
}
