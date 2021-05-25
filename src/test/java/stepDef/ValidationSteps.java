package stepDef;

import org.junit.Assert;
import org.junit.runner.Request;

import PageObject.AddPlace;
import PageObject.Location;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.ApiResources;
import resources.TestDataBuild;
import resources.Utils;

import static io.restassured.RestAssured.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

	public class ValidationSteps extends Utils {
	
	ResponseSpecification resspec;
	Response response;
	RequestSpecification res;
	TestDataBuild data = new TestDataBuild();
	static String place_ID;
	
	@Given("Add Place Payload with {string} {string} {string}")
	public void add_Place_Payload_with(String name, String language, String address) throws IOException{
		
		res = given().spec(requestspecification())
		.body(data.addPlacePayload(name, language, address));
	}

	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String httpMethod) {
		
		resspec =new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectContentType(ContentType.JSON)
				.build();
		//constructor will be called with the value of resource which we pass
		System.out.println(ApiResources.valueOf(resource).getResource());
		
		if(httpMethod.equalsIgnoreCase("POST")) {
		response = res.when().post(ApiResources.valueOf(resource).getResource()).
				then().spec(resspec).extract().response();
		}
		else if(httpMethod.equalsIgnoreCase("GET")) {
			response = res.when().get(ApiResources.valueOf(resource).getResource()).
					then().spec(resspec).extract().response();
		}
		
	
	}
 
	@Then("the API call got success with status code {int}")
	public void the_API_call_got_success_with_status_code(Integer int1) {
	  Assert.assertEquals(response.getStatusCode(), 200);
	}

	@Then("{string} in response body is {string}")
	public void in_response_body_is(String keyValue, String expectedValue) {
		
		Assert.assertEquals(getJsonPath(response, keyValue),expectedValue);
	}
	
	@Then("verify placeID created maps to {string} using {string}")
	public void verify_placeID_created_maps_to_using(String ExpName, String resource) throws IOException {
		place_ID = getJsonPath(response, "place_id");
		
		res = given().spec(requestspecification()).queryParam("place_id",place_ID);
		
		user_calls_with_http_request(resource,"GET");
		
		String ActName = getJsonPath(response, "name"); 
		Assert.assertEquals(ActName, ExpName); 	
	}

	@Given("DeletePlace Payload")
	public void deleteplace_Payload() throws IOException {
	  res = given().spec(requestspecification()).body(data.deletePlacePayload(place_ID));
	}


}
