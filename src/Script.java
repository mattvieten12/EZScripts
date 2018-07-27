import java.util.ArrayList;

public class Script {

	private ArrayList<Website> scriptSites;
	private ArrayList<App> scriptFiles;
	
	public Script(ArrayList<Website> scriptSites, ArrayList<App> scriptFiles) {
		this.scriptSites = scriptSites;
		this.scriptFiles = scriptFiles;
	}
	
	public ArrayList<Website> getWebsites() {
		return scriptSites;
	}
	
	public ArrayList<App> getFiles() {
		return scriptFiles;
	}
}
