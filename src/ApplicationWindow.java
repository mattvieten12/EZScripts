
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
	private static TextField websiteField;
	public static void main(String[] args) {
		launch(args);
	}
	//starting point for the application
	//this is where we put the code for the user interface
	@Override
	public void start(Stage primaryStage) throws IOException {

		newWebsites = new ArrayList<Website>();
		//The primaryStage is the top-level container
		primaryStage.setTitle("Easy-Script-Generator");
		BorderPane componentLayout = new BorderPane();
		componentLayout.setPadding(new Insets(20,0,20,20));


		//The FlowPane is a conatiner that uses a flow layout
		FlowPane textPane = new FlowPane();
		textPane.setHgap(100);
		Label choiceLbl = new Label("Website");


		websiteField = new TextField("https://");
		websiteField.setDisable(false);

		Button submitBtn = new Button("Create Shortcut!");

		textPane.getChildren().add(choiceLbl);
		textPane.getChildren().add(websiteField);

		componentLayout.setTop(textPane);

		submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					createBatch();
					FlowPane websitePane = addLabels();
					componentLayout.setCenter(websitePane);
				} catch (IOException e) {
					System.out.print("Could not generate the shortcut.");
					e.printStackTrace();
				}
			}
		});


		componentLayout.setBottom(submitBtn);
		//Add the BorderPane to the Scene
		Scene appScene = new Scene(componentLayout,500,500);
		//Add the Scene to the Stage
		primaryStage.setScene(appScene);
		primaryStage.show();
	}

	public static FlowPane addLabels() {
		FlowPane websitePane = new FlowPane();
			for (Website website: newWebsites) {
				Label websiteLabel = new Label(website.getLabel());
				websitePane.getChildren().add(websiteLabel);
				newWebsites.remove(website);
			}
		return websitePane;
	}

	public static void createBatch() throws IOException { 
		File file=new File("C:\\Users\\Public\\Desktop\\test.bat"); 
		FileOutputStream fos=new FileOutputStream(file); 
		DataOutputStream dos=new DataOutputStream(fos); 
		Website website = new Website(websiteField.getText());
		dos.writeBytes("start " + website.getURL()); 
		newWebsites.add(website);
		websiteField.clear();
		//dos.writeBytes("START note.txt"); 
		file.setReadOnly();
		dos.close();
		fos.close();
		ShellLink.createLink("C:\\Users\\Public\\Desktop\\test.bat", "C:\\Users\\Public\\Desktop\\test.lnk");
	} 
}