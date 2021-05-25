package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {

	public static RequestSpecification req;										//java will not make it available for whole run
	
	public RequestSpecification requestspecification() throws IOException {
		 
		if(req==null) {  														   //this is because we dont want to overwrite our previous testcase results
		PrintStream stream = new PrintStream(new FileOutputStream("logging.txt")); // for logging request and response
		req =new RequestSpecBuilder()
				.setBaseUri(getglobalValue("baseUrl"))
				.addQueryParam("key", "qaclick123")
				.addFilter(RequestLoggingFilter.logRequestTo(stream))
				.addFilter(ResponseLoggingFilter.logResponseTo(stream))
				.setContentType(ContentType.JSON).build();
		}
		return req;
	}
	
	public String getglobalValue(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream("D:\\Eclipse_Workspace I\\ApiFramework\\src\\test\\java\\resources\\global.properties");
		prop.load(fis);
		return prop.getProperty("baseUrl");
	}
	
	public String getJsonPath(Response response, String Key) {
		
		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		return js.get(Key).toString();
		
	}
}
