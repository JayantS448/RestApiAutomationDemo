package resources;
//enum is a special class in java which has collection of constants and methods
public enum ApiResources {

	AddPlaceAPI("/maps/api/place/add/json"),
	GetPlaceAPI("/maps/api/place/get/json"),
	DeletePlaceAPI("/maps/api/place/delete/json");

	private String resource;

	
	ApiResources(String string) {
		// TODO Auto-generated constructor stub
		this.resource = string;
	}
	
	public String getResource() {
		return resource;
	}
}
