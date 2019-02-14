package DatabaseConnector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;

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
     */
    public static JSONArray parseData (String json) throws JSONErrorException {
        JSONObject obj = getJSONObject(json);

        String status = (String) obj.get("status");
        if(status.equals("error")) {
            //if status is error, then only the error metadata is sent, thus get the message
            throw new JSONErrorException((String)obj.get("message"));
        }

        //as the root node is an array, we will cast the JSONObject obj to a JSONArray object
        JSONArray arr = (JSONArray) obj.get("data");

        return arr;
    }

    /**
     * @param arr JSONArray object containing all the database data
     * @return Results object containing all the database data that can be easily accessed
     *
     * This method takes a JSON array and inputs all the data into the object, including the column headers
     */
    public static Results getResults(JSONArray arr) {
        JSONObject firstRow = (JSONObject) arr.get(0); //get first object
        Object[] keyArr = firstRow.keySet().toArray();
        int rows = arr.size();
        int cols = keyArr.length;

        //we need to cast the keyArr to a string array
        String[] keys = new String[cols];

        for(int i = 0; i < cols; i ++) {
            keys[i] = keyArr[i].toString();
        }

        //define the results object
        Results res = new Results(rows, cols);
        res.setColumn(keys);

        //Iterate through the JSON array and store the data in res
        Iterator it = arr.iterator();
        int currentRow = 0;
        while(it.hasNext()) {
            JSONObject currentObject = (JSONObject)it.next();

            //we need to loop through each object and retireve all the keys
            for(int i = 0; i < cols; i++) {
                res.setElement(currentRow, i, currentObject.get(keys[i]));
            }
            currentRow++;
        }

        return res;
    }

    public static boolean checkSucceeded(JSONObject jsonObj) throws JSONErrorException {
        String status = (String) jsonObj.get("status");

        if(status.equals("success")) {
            return true;
        } else if(status.equals("error")) {
            throw new JSONErrorException((String)jsonObj.get("message"));
        }
        return false;
    }

}
