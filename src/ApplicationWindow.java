
//Imports are listed in full to show what's being used
//could just import javafx.*
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import mslinks.ShellLink;


public class ApplicationWindow extends Application {

	private static ArrayList<Website> newWebsites;
	private static ArrayList<Website> scriptSites;
	private static TextField websiteField;
	private static TextField scriptName;
	private static FlowPane websitePane;
	private static TextArea websiteLabels;
	private static FlowPane textPane;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	//starting point for the application
	//this is where we put the code for the user interface
	@Override
	public void start(Stage primaryStage) throws IOException {

		newWebsites = new ArrayList<Website>();
		scriptSites = new ArrayList<Website>();
		
		websitePane = new FlowPane();
		
		websiteField = new TextField("https://");
		scriptName = new TextField();
		websiteLabels = new TextArea();
		
		
		websiteLabels.setPrefSize(250, 150);
		websiteLabels.setEditable(false);
		websiteLabels.setStyle("-fx-font-weight: bold");
		
		websitePane.getChildren().add(websiteLabels);
		websitePane.setPadding(new Insets(20, 20, 20, 0));
		
		
		//The primaryStage is the top-level container
		primaryStage.setTitle("Easy-Script-Generator");
		BorderPane componentLayout = new BorderPane();
		componentLayout.setPadding(new Insets(20,0,20,20));


		//The FlowPane is a conatiner that uses a flow layout
		textPane = new FlowPane();
		textPane.setHgap(10);
		Label choiceLbl = new Label("Website");
		
		Button submitBtn = new Button("Create Shortcut!");
		
		textPane.getChildren().add(choiceLbl);
		textPane.getChildren().add(websiteField);
		textPane.getChildren().add(scriptName);

		componentLayout.setTop(textPane);
		componentLayout.setCenter(websitePane);
		
		
		
		submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					createBatch();
					addLabels();
				} catch (IOException e) {
					System.out.print("Could not generate the shortcut.");
					e.printStackTrace();
				}
			}
		});


		componentLayout.setBottom(submitBtn);

		
		Scene appScene = new Scene(componentLayout,500,500);
		primaryStage.setScene(appScene);
		primaryStage.show();
	}

	public static void addLabels() {
			for (Website website: newWebsites) {
				websiteLabels.appendText(website.getLabel() + "\n");
			}
			newWebsites.clear();
	}
	
	
	public static void addWebsiteToList(Website website) {
		
	}

	public static void createBatch() throws IOException { 
		Website website = new Website(websiteField.getText(), scriptName.getText());
		
		File file=new File("C:\\Users\\Public\\Desktop\\" + website.getLabel() + ".bat"); 
		FileOutputStream fos=new FileOutputStream(file); 
		DataOutputStream dos=new DataOutputStream(fos); 

		dos.writeBytes("start " + website.getURL()); 
		newWebsites.add(website);
		scriptSites.add(website);
		websiteField.clear();
		scriptName.clear();
		file.setReadOnly();
		dos.close();
		fos.close();
		ShellLink.createLink("C:\\Users\\Public\\Desktop\\" + website.getLabel() + ".bat", "C:\\Users\\Public\\Desktop\\" + website.getLabel()+ ".lnk");
	} 
}