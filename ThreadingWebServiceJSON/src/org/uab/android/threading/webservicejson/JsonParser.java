package org.uab.android.threading.webservicejson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 
 * JSON parser class. Parses incoming data from donkikochan.uab.cat web service
 *
 */
public class JsonParser {
	
	private static final String DATA_JSON_ARRAY = "data";
	private static final String ID_STRING = "id";
	
	private String jsonString;

	public JsonParser() {  }
	
	public JsonParser(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	/**
	 * Parses the JSON String stored in the local variable jsonString.
	 * 
	 * @return A human readable String obtained from the JSON String.
	 * 
	 * @throws JSONException
	 */
	public String parse() throws JSONException {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		// Create a new instance of the JSONTokener object
		JSONTokener jsonTokener = new JSONTokener(this.jsonString);
		
		// Get the root element of the parsing JSON
		JSONObject rootObject = (JSONObject) jsonTokener.nextValue();
		
		// Extract the "data" JSONArray from the response JSONObject
		JSONArray dataArray = rootObject.getJSONArray(DATA_JSON_ARRAY);
		
		// Extract the JSON objects contained in the "data" array
		for( int i = 0; i < dataArray.length(); i++ ) {
			
			JSONObject dataObject = dataArray.getJSONObject(i);
			
			// Get the "id" values of the current JSON Object
			// Note that such instruction is valid only if the JSONObject contains such a key
			String ID = dataObject.getString(ID_STRING);
			
			stringBuilder.append(ID).append("\n");
		}
		
		return stringBuilder.toString();
	}
}
