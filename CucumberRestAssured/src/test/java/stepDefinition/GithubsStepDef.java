package stepDefinition;

import java.io.IOException;

import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utility.payLoadConverter;

public class GithubsStepDef {
	
	public static String baseURL = "https://api.github.com" ;
	public static String bearer_token = "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" ;
	public static String createRepoDetails ;
	public static String ownerName ;
	public static String RepoName ;
	public static String autolinkPayload ;
	public static int autolinkID ;
	public static String textFilePayload ;
	public static String sha ;
	public static String deleteFilebody ;
	
	RequestSpecification requestSpecification ;
	Response response ;
	
	
//	CREATE A REPOSITORY FOR A AUTHENTICATED USER


	@Given("Get Payload from {string} for repo")
	public void get_payload_from_for_repo(String jsonfilename) throws IOException {
		createRepoDetails = payLoadConverter.generatePayloadString(jsonfilename) ;
		System.out.println(createRepoDetails);
	}
	@When("Create Repo {string} and provide token")
	public void create_repo_and_provide_token(String resource) {
	   requestSpecification = RestAssured.given().body(createRepoDetails) ;
	   requestSpecification.contentType(ContentType.JSON) ;
	   requestSpecification.header("Authorization", "Bearer " + bearer_token);
	   response = requestSpecification.post(baseURL + resource ) ;
	}
	
	@Then("Successfully Create with status code {int}")
	public void successfully_create_with_status_code(int statusCode) {
		Assert.assertEquals( response.getStatusCode(), statusCode);
	}
	
	@Then("Varify owner Name")
	public void varify_owner_name() {
		
		String result = response.asPrettyString() ;
		System.out.println(result);
		
//		Getting Owner Name
		JsonPath Ownerjs = new JsonPath(result);
		String fullName = Ownerjs.get("full_name") ;
		String[] arrOfStr = fullName.split("/");
		ownerName = arrOfStr[0] ;
		System.out.println("Owner Name - " + ownerName);
		
//		Getting Repository Name
		 RepoName = Ownerjs.get("name") ;
		 System.out.println("Repository Name - " + RepoName);
	}



	
//	CREATE AN AUTOLINK REFERENCE FOR A REPOSITORY
	

	@Given("Should have repository and owner name")
	public void should_have_repository_and_owner_name() throws IOException {
		autolinkPayload = payLoadConverter.generatePayloadString("Autolink.json") ;
	}
	
	@When("Create an Autolink reference {string} owner \\/ repo {string} for a repository")
	public void create_an_autolink_reference_owner_repo_for_a_repository(String resRepo, String resAutolink) {
		
		requestSpecification = RestAssured.given().body(autolinkPayload) ;
		requestSpecification.contentType(ContentType.JSON) ;
		requestSpecification.header("Authorization", "Bearer " + bearer_token);
		response = requestSpecification.post(baseURL + resRepo + ownerName + "/" + RepoName + resAutolink ) ;
	}
	
	@Then("Successfully Create an Autolink reference with status code {int}")
	public void successfully_create_an_autolink_reference_with_status_code(int statusCode) {
		Assert.assertEquals( response.getStatusCode(), statusCode);
	}
	
	@Then("Varify an Autolink id")
	public void varify_an_autolink_id() {
		String result = response.asPrettyString() ;
		System.out.println(result);
		
//		Getting Autolink id
		JsonPath js = new JsonPath(result);
		autolinkID = js.get("id") ;
		System.out.println(autolinkID);
	}





//	GET AN AUTOLINK REFERENCE FOR A REPOSITORY
	

	@Given("should have an Autolink id")
	public void should_have_an_autolink_id() {
		System.out.println( "Autolink id - " + autolinkID );
	}
	
	@When("Get an Autolink reference {string} owner \\/ repo {string} id for a repository")
	public void get_an_autolink_reference_owner_repo_id_for_a_repository(String resRepo, String resAutolink) {
		
		requestSpecification = RestAssured.given() ;
		requestSpecification.contentType(ContentType.JSON) ;
		requestSpecification.header("Authorization", "Bearer " + bearer_token);
		response = requestSpecification.get(baseURL + resRepo + ownerName + "/" + RepoName + resAutolink + autolinkID) ;
	}
	
	@Then("Successfully get an Autolink reference with status code {int}")
	public void successfully_get_an_autolink_reference_with_status_code(int statusCode) {
		Assert.assertEquals( response.getStatusCode(), statusCode);
	}
	
	@Then("Verify response")
	public void verify_response() {
		String result = response.asPrettyString() ;
		System.out.println(result);
	}




//	CREATE OR UPDATE FILE CONTENT
	

	@Given("Get Payload from {string} for create a file")
	public void get_payload_from_for_create_a_file(String jsonfilename) throws IOException {
		textFilePayload = payLoadConverter.generatePayloadString(jsonfilename) ;
	}
	
	@When("Create or update Execution {string} owner \\/ repo {string} \\/ {string}")
	public void create_or_update_execution_owner_repo(String resRepo, String contents, String txtFileName) {
		requestSpecification = RestAssured.given().body(textFilePayload) ;
		requestSpecification.contentType(ContentType.JSON) ;
		requestSpecification.header("Authorization", "Bearer " + bearer_token);
		response = requestSpecification.put(baseURL + resRepo + ownerName + "/" + RepoName + contents + txtFileName) ;
	}
	
	@Then("Successfully create a file with status code {int}")
	public void successfully_create_a_file_with_status_code(int statusCode) {
		Assert.assertEquals( response.getStatusCode(), statusCode);
	}
	
	@Then("Verify sha")
	public void verify_sha() {
		String result = response.asString() ;
		System.out.println(result);
		
//		getting sha
		JsonPath js = new JsonPath(result);
		sha = js.get("content.sha") ;
		System.out.println(sha);
	}




//	DELETE A FILE
	

	@Given("should have a file in Repository")
	public void should_have_a_file_in_repository() {
		System.out.println( "sha - " +  sha);
		 deleteFilebody = "{\r\n"
				+ "    \"message\": \"Create new file\",\r\n"
				+ "    \"sha\": \""+sha+"\"\r\n"
				+ "}";
	}
	
	@When("Delete a file {string} owner \\/ repo {string} \\/ {string}")
	public void delete_a_file_owner_repo(String resRepo, String contents, String txtFileName) {
		requestSpecification = RestAssured.given().body(deleteFilebody) ;
		requestSpecification.contentType(ContentType.JSON) ;
		requestSpecification.header("Authorization", "Bearer " + bearer_token);
		response = requestSpecification.delete(baseURL + resRepo + ownerName + "/" + RepoName + contents + txtFileName) ;
	}
	
	@Then("Successfully delete a file with status code {int}")
	public void successfully_delete_a_file_with_status_code(int statusCode) {
		Assert.assertEquals( response.getStatusCode(), statusCode);
	}



	

//	DELETE FROM AN AUTOLINK REFERENCE FOR A REPOSITORY


	@Given("should have Autolink id")
	public void should_have_autolink_id() {
		System.out.println( "Autolink id - " + autolinkID );
	}
	
	@When("delete an Autolink reference {string} owner \\/ repo {string} id for a repository")
	public void delete_an_autolink_reference_owner_repo_id_for_a_repository(String resRepo, String resAutolink) {
		requestSpecification = RestAssured.given() ;
		requestSpecification.contentType(ContentType.JSON) ;
		requestSpecification.header("Authorization", "Bearer " + bearer_token);
		response = requestSpecification.delete(baseURL + resRepo + ownerName + "/" + RepoName + resAutolink + autolinkID) ;
	}
	
	@Then("Successfully delete an Autolink reference with status code {int}")
	public void successfully_delete_an_autolink_reference_with_status_code(int statusCode) {
		Assert.assertEquals( response.getStatusCode(), statusCode);
	}


}
