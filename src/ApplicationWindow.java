
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import mslinks.ShellLink;


public class ApplicationWindow extends Application {

	private static Website newestWebsite;
	private static ArrayList<Website> scriptSites;
	private static TextField websiteURL;
	private static TextField websiteName;
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

		newestWebsite = null;
		scriptSites = new ArrayList<Website>();

		websitePane = new FlowPane();

		websiteURL = new TextField("https://");
		websiteName = new TextField();
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


		textPane = new FlowPane();
		textPane.setHgap(10);
		Label urlLabel = new Label("URL");

		Button submitBtn = new Button("Create Shortcut!");
		Button addWebsiteBtn = new Button("Add website!");

		textPane.getChildren().add(urlLabel);
		textPane.getChildren().add(websiteURL);
		textPane.getChildren().add(websiteName);

		componentLayout.setTop(textPane);
		componentLayout.setCenter(websitePane);



		addWebsiteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addWebsiteToList(new Website(websiteURL.getText(), websiteName.getText()));
			}
		});

		submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(primaryStage);
				BorderPane popupPane = new BorderPane();
				popupPane.getChildren().add(new Text("Script Generate Confirmation"));
				Scene dialogScene = new Scene(popupPane, 300, 200);
				dialog.setScene(dialogScene);
				dialog.show();

				scriptName = new TextField();
				Label scriptNameLabel = new Label("Pick a name for your generated script!");

				FlowPane scriptPane = new FlowPane();
				scriptPane.getChildren().addAll(scriptNameLabel);
				scriptPane.getChildren().addAll(scriptName);
				popupPane.setCenter(scriptPane);
				Button popupOKBtn = new Button("OK");

				popupOKBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						dialog.hide();
						try {
							createBatch();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				FlowPane bottomPane = new FlowPane();
				bottomPane.getChildren().add(popupOKBtn);
				popupPane.setBottom(bottomPane);
			}
		});


		FlowPane bottomPane = new FlowPane();
		bottomPane.getChildren().add(addWebsiteBtn);
		bottomPane.getChildren().add(submitBtn);
		componentLayout.setBottom(bottomPane);


		Scene appScene = new Scene(componentLayout,500,500);
		primaryStage.setScene(appScene);
		primaryStage.show();
	}


	public static void addWebsiteToList(Website website) {
		newestWebsite = website;
		scriptSites.add(website);
		websiteURL.clear();
		websiteLabels.appendText(newestWebsite.getLabel() + "\n");
	}

	public static void createBatch() throws IOException { 
		File file=new File("C:\\Users\\Public\\Desktop\\" + scriptName.getText() + ".bat"); 
		FileOutputStream fos=new FileOutputStream(file); 
		DataOutputStream dos=new DataOutputStream(fos); 
		String newLine = System.getProperty("line.separator");
		for (Website website: scriptSites) {
			dos.writeBytes("call start " + website.getURL()); 
			dos.writeBytes(newLine);
			websiteName.clear();
			file.setReadOnly();
		}
		dos.close();
		fos.close();
		ShellLink.createLink("C:\\Users\\Public\\Desktop\\" + scriptName.getText() + ".bat", "C:\\Users\\Public\\Desktop\\" + scriptName.getText() + ".lnk");
	} 
}