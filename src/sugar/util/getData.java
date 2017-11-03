package sugar.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.simple.JSONObject;

public class getData{
	

	static String leadData = null;
	static String url = "http://yellowfinbi.southeastasia.cloudapp.azure.com/sugarce/service/v4_1/rest.php";
	
	public static void main(String[] args) throws NoSuchAlgorithmException, ClientProtocolException, IOException {
		String sessionId = Authenticate.authenticate("admin","admin",url);
		
		JSONArray oppField = new JSONArray();
		oppField.put("name");
		oppField.put("id");
		oppField.put("date_entered");
		oppField.put("lead_source");
		oppField.put("amount");
		oppField.put("account_name");
		oppField.put("sales_stage");
		oppField.put("date_closed");
		oppField.put("opportunity_type");
		
		oppField.put("description");
		oppField.put("deleted");
		oppField.put("amount_usdollar");
		oppField.put("next_step");
		oppField.put("probability");
		oppField.put("date_modified");
		
		JSONArray leadField1 = new JSONArray();
		
		leadData = retreiveEntriesByModule(sessionId,url, "Opportunities",oppField);
		
		System.out.println(leadData);
		
		/*FileWriter file1 = new FileWriter(new File("/home/aptus/lead.txt"));
		file1.write(leadData);
		file1.flush();
		file1.close();*/
		
	}
	
	public static String retreiveEntriesByModule(String sessionID,String url, String moduleName,JSONArray selectedField) throws ClientProtocolException, IOException
	{
			/*JSONArray grades = new JSONArray();
			grades.put("name");
			grades.put("id");*/
			JSONArray grades = new JSONArray();		
			Map<String, Object> list_data = new LinkedHashMap<String, Object>();
	    	list_data.put("session", sessionID);
	    	list_data.put("module_name", moduleName);
	    	list_data.put("query", "");
	    	list_data.put("order_by", "");
	    	list_data.put("offset", "0");
	    	list_data.put("select_fields", selectedField);
	    	list_data.put("link_name_to_fields_array", grades);
	    	list_data.put("max_results","10000");
	    	list_data.put("deleted", "0");
	    	list_data.put("Favorites", false);
	    	
			Authenticate.multipartEntity = MultipartEntityBuilder.create();    	
			Authenticate.multipartEntity.addTextBody("method","get_entry_list",ContentType.TEXT_PLAIN);
	    	
	    	// define request encoding
			Authenticate.multipartEntity.addTextBody("input_type","JSON",ContentType.TEXT_PLAIN);
	    	
	    	// define response encoding
			Authenticate.multipartEntity.addTextBody("response_type","JSON",ContentType.TEXT_PLAIN);
	    	
	    	//System.out.println(JSONObject.toJSONString(list_data));
	    	Authenticate.multipartEntity.addTextBody("rest_data", (JSONObject.toJSONString(list_data)),ContentType.TEXT_PLAIN);
	    	
	    	Authenticate.httpPost = new HttpPost(url);
	    	
	    	Authenticate.multipart=Authenticate.multipartEntity.build();
	    	Authenticate.httpPost.setEntity(Authenticate.multipart);
	    	
	    	Authenticate.defaultHttpClient = HttpClients.createDefault();
	    	Authenticate.response=Authenticate.defaultHttpClient.execute(Authenticate.httpPost);
	    	
	    	//HttpEntity entity = response.getEntity();
	    	String jsonString = EntityUtils.toString(Authenticate.response.getEntity());
			return jsonString;
	}

}