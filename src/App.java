
public class App {
	
	private String path;
	private String label;
	
	public App(String path, String label) {
		this.path = path;
		this.label = label;
	}
	
	public App(String path) {
		this(path, null);
	}
	
	public String getPath() {
		return path;
	}
	
	public String getLabel() {
		return label;
	}
}
