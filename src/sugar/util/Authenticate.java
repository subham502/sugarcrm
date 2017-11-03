package sugar.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Authenticate {
		
	public static CloseableHttpClient defaultHttpClient = null;
	public static CloseableHttpResponse response= null;
	public static HttpResponse execute = null;
	public static MultipartEntityBuilder multipartEntity = null;
	public static HttpPost httpPost = null;
	public static HttpEntity multipart = null;
	public static HttpEntity entity = null;
	public static String url = null;
	public static String name_user = null;
	
	public static String sessionID;
	public static String authenticate(String user,String pass,String url) throws NoSuchAlgorithmException, ClientProtocolException, IOException
	{		
		 MessageDigest md5 = MessageDigest.getInstance("MD5");
	     String passwordHash = new BigInteger(1, md5.digest(pass.getBytes())).toString(16);
	     
    	// the order is important, so use a ordered map
    	Map<String, String> userCredentials = new LinkedHashMap<String, String>();
    	userCredentials.put("user_name", user);
    	userCredentials.put("password", passwordHash);
    	
    	Map<String, Object> request = new LinkedHashMap<String, Object>();
	 
    	request.put("user_auth", userCredentials);
    	request.put("application_name", "RestClient");
    	
    	multipartEntity = MultipartEntityBuilder.create(); 
    	
    	multipartEntity.addTextBody("method","login", ContentType.TEXT_PLAIN);
    	
    	// define request encoding
    	multipartEntity.addTextBody("input_type","JSON",ContentType.TEXT_PLAIN);
    	
    	// define response encoding
    	multipartEntity.addTextBody("response_type","JSON",ContentType.TEXT_PLAIN);
    	multipartEntity.addTextBody("rest_data",JSONObject.toJSONString(request),ContentType.TEXT_PLAIN);
    	
    	httpPost = new HttpPost(url);
    	
    	multipart=multipartEntity.build();
    	httpPost.setEntity(multipart);
    	
    	defaultHttpClient = HttpClients.createDefault();
    	response=defaultHttpClient.execute(httpPost);
    	execute = defaultHttpClient.execute(httpPost);
    	   	
    	entity = response.getEntity();
    	//System.out.println(EntityUtils.toString(response.getEntity()));
    	//System.out.println(entity.getContent());
    	JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(entity.getContent()));
    	sessionID=(String) parse.get("id");
		 
		return sessionID;
	}	
	public String getSessionID()
	{
		return this.sessionID;
	}	
	public void closeConnection() throws IOException
	{
		response = null;
		execute = null;
		if(defaultHttpClient != null)
		{
			defaultHttpClient.close();
			defaultHttpClient = null;
		}
		else
			defaultHttpClient = null;
		multipartEntity = null;
		httpPost = null;
		multipart = null;
		entity = null;
	}
}
