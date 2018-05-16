
public class Website {

	private String url;
	private String label;
	
	public Website(String url, String label) {
		this.url = url;
		this.label = label;
	}
	
	public Website(String url) {
		this(url, null);
	}
	
	public String getURL() {
		return url;
	}
	
	public String getLabel() {
		return label;
	}
}
