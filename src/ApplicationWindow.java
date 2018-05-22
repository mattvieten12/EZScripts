
//Imports are listed in full to show what's being used
//could just import javafx.*
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private static TextField scriptName;
	private static FlowPane websitePane;
	private static ListView<String> websiteLabelsListView;
	private static FlowPane textPane;

	private static String OS;

	public static void main(String[] args) {
		launch(args);
	}
	//starting point for the application
	//this is where we put the code for the user interface
	@Override
	public void start(Stage primaryStage) throws IOException {


		System.out.println(System.getProperty("user.home"));
		newestWebsite = null;
		scriptSites = new ArrayList<Website>();

		websitePane = new FlowPane();

		websiteURL = new TextField("https://");
		websiteLabelsListView = new ListView<String>();


		websiteLabelsListView.setPrefSize(250, 150);
		websiteLabelsListView.setEditable(false);
		websiteLabelsListView.setStyle("-fx-font-weight: bold");

		websitePane.getChildren().add(websiteLabelsListView);
		websitePane.setPadding(new Insets(20, 20, 20, 0));


		//The primaryStage is the top-level container
		primaryStage.setTitle("Easy-Script-Generator");
		BorderPane componentLayout = new BorderPane();
		componentLayout.setPadding(new Insets(20,0,20,20));


		textPane = new FlowPane();
		textPane.setHgap(10);
		Label urlLabel = new Label("URL");


		textPane.getChildren().add(urlLabel);
		textPane.getChildren().add(websiteURL);


		final Button removeWebsiteButton = new Button("Remove Selected");
		final Button updateWebsiteLVButton = new Button("Update Selected");
		final Button addWebsiteButton = new Button("Add website!");
		final Button updateWebsiteURLButton = new Button("Update");
		final Button submitButton = new Button("Create Shortcut!");


		removeWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				String websiteName = websiteLabelsListView.getSelectionModel().getSelectedItem();
				if (websiteName != null) {
					for (Website website: scriptSites) {
						if (websiteName.equals(website.getLabel())) {
							System.out.println("hello");
							removeWebsiteFromList(website);
							break;
						}
					}
				}
			}
		});


		addWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String strURL = websiteURL.getText();
				String regex = "^(((https?|ftp|file)://)|(www.))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(strURL);
				if (strURL != null && m.find()) {
					removeWebsiteButton.setVisible(true);
					updateWebsiteLVButton.setVisible(true);
					String websiteName = websiteURL.getText().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
					addWebsiteToList(new Website(websiteURL.getText(), websiteName));
				}
			}
		});

		textPane.getChildren().add(addWebsiteButton);

		updateWebsiteLVButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				String websiteToUpdateName = websiteLabelsListView.getSelectionModel().getSelectedItem();
				if (websiteToUpdateName != null) {
					addWebsiteButton.setVisible(false);
					addWebsiteButton.setManaged(false);
					updateWebsiteURLButton.setVisible(true);
					updateWebsiteURLButton.setManaged(true);
					for (Website website: scriptSites) {
						if (websiteToUpdateName.equals(website.getLabel())) {
							websiteURL.setText(website.getURL());

							break;
						}
					}

					updateWebsiteURLButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							String websiteToAddName = websiteURL.getText().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
							Integer selectedIndex = websiteLabelsListView.getSelectionModel().getSelectedIndex();
							Website tempWebsite = new Website(websiteURL.getText(), websiteToAddName);
							scriptSites.add(tempWebsite);
							websiteURL.clear();
							websiteLabelsListView.getItems().add(selectedIndex, tempWebsite.getLabel());

							for (Website website: scriptSites) {
								if (websiteToUpdateName.equals(website.getLabel())) {
									removeWebsiteFromList(website);
									break;
								}
							}
							updateWebsiteURLButton.setVisible(false);
							updateWebsiteURLButton.setManaged(false);
							addWebsiteButton.setManaged(true);
							addWebsiteButton.setVisible(true);
						}
					});
				}
			}
		});

		updateWebsiteURLButton.setVisible(false);
		updateWebsiteURLButton.setManaged(false);
		textPane.getChildren().add(updateWebsiteURLButton);
		
		
		removeWebsiteButton.setVisible(false);
		updateWebsiteLVButton.setVisible(false);

		BorderPane middleRightPane = new BorderPane();
		middleRightPane.setTop(removeWebsiteButton);
		middleRightPane.setCenter(updateWebsiteLVButton);
		websitePane.getChildren().add(middleRightPane);


		componentLayout.setTop(textPane);
		componentLayout.setCenter(websitePane);

		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (scriptSites.isEmpty() == false) {
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
					
					FlowPane bottomPane = new FlowPane();
					
					popupOKBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (scriptName != null) {
								dialog.hide();
								try {
									createBatch();
									websiteLabelsListView.getItems().clear();
									scriptSites = new ArrayList<Website>();
									newestWebsite = null;

								} catch (FileNotFoundException e) {
									Label label = new Label("File already exists, please select a different name.");
									label.setTextFill(Color.RED);
									bottomPane.getChildren().add(label);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					});
					
					bottomPane.getChildren().add(popupOKBtn);
					popupPane.setBottom(bottomPane);
				}
			}
		});


		FlowPane bottomPane = new FlowPane();
		//bottomPane.getChildren().add(addWebsiteBtn);
		bottomPane.getChildren().add(submitButton);
		componentLayout.setBottom(bottomPane);


		Scene appScene = new Scene(componentLayout,500,500);
		primaryStage.setScene(appScene);
		primaryStage.show();
	}


	public static void addWebsiteToList(Website website) {
		newestWebsite = website;
		scriptSites.add(website);
		websiteURL.clear();
		websiteLabelsListView.getItems().addAll(newestWebsite.getLabel());
	}

	public static void removeWebsiteFromList(Website website) {
		scriptSites.remove(website);
		websiteLabelsListView.getItems().remove(website.getLabel());
	}

	public static void updateWebsiteFromList(Website website) {
		websiteURL.setText(website.getURL());

	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static void createBatch() throws IOException, FileNotFoundException { 


		OS = System.getProperty("os.name").toLowerCase();

		System.out.println(OS);

		if (isWindows()) {
			File file=new File("C:\\Users\\Public\\Desktop\\" + scriptName.getText() + ".bat"); 
			FileOutputStream fos=new FileOutputStream(file); 
			DataOutputStream dos=new DataOutputStream(fos); 
			String newLine = System.getProperty("line.separator");
			for (Website website: scriptSites) {
				if (scriptSites.get(0) == website) {
					dos.writeBytes("call start /w chrome.exe -new-window " + website.getURL()); 
					dos.writeBytes(newLine);
				}
				else {
					dos.writeBytes("call start /w chrome.exe " + website.getURL()); 
				}
				dos.writeBytes(newLine);
				file.setReadOnly();
			}
			dos.close();
			fos.close();
			ShellLink.createLink("C:\\Users\\Public\\Desktop\\" + scriptName.getText() + ".bat", "C:\\Users\\Public\\Desktop\\" + scriptName.getText() + ".lnk");
		} else if (isMac()) {
			File file = new File(System.getProperty("user.home") + "/Desktop/" + scriptName.getText() + ".command"); 
			FileOutputStream fos=new FileOutputStream(file); 
			DataOutputStream dos=new DataOutputStream(fos); 
			String newLine = System.getProperty("line.separator");
			dos.writeBytes("#!/bin/bash");
			dos.writeBytes(newLine);
			for (Website website: scriptSites) {
				if (scriptSites.get(0) == website) {
					dos.writeBytes("open -na 'Google Chrome' --args --new-window " + website.getURL()); 
					dos.writeBytes(newLine);
					file.setReadOnly();
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
				else if (scriptSites.get(scriptSites.size() - 1) == website) {
					dos.writeBytes("open -na 'Google Chrome' " + website.getURL()); 
				}
				else {
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
					dos.writeBytes("open -na 'Google Chrome' " + website.getURL()); 
					dos.writeBytes(newLine);
				}
			}
			dos.close();
			fos.close();
			file.setExecutable(true);
		} else {
			System.out.println("Your OS is not supported!!");
		}
	}
}