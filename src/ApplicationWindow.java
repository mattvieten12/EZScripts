
//Imports are listed in full to show what's being used
//could just import javafx.*
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aquafx_project.AquaFx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ApplicationWindow extends Application {


	private static TextField websiteURL;
	private static TextField filePath;

	private static File fileChosen;

	private static TabPane tabPane;
	private static Tab createTab;

	private static FlowPane websiteListPane;
	private static FlowPane fileListPane;

	private static FlowPane websiteURLPane;
	private static FlowPane filePathPane;

	private static ArrayList<Website> scriptSites;
	private static ArrayList<App> scriptFiles;

	private static ListView<String> websiteLabelsListView;
	private static ListView<String> fileLabelsListView;

	private static TextField scriptName;

	private static Scene appScene;
	private static Stage primaryStage;

	final static Button removeWebsiteButton = new Button("Remove Website");
	final static Button updateWebsiteLVButton = new Button("Update Website");
	final static Button addWebsiteButton = new Button("Add website!");
	final static Button updateWebsiteURLButton = new Button("Update");
	final static Button clearWebsiteButton = new Button("Clear All Websites");
	final static Button createScriptButton = new Button("Create Shortcut!");

	final static Button browseFilesButton = new Button("Browse files...");
	final static Button addFileButton = new Button("Add File!");
	final static Button removeFileButton = new Button("Remove File");
	final static Button updateFileLVButton = new Button("Update File");
	final static Button clearFileButton = new Button("Clear All Files");
	final static Button updateFilePathButton = new Button("Update!");
	final static Button popupCreateButton = new Button("CREATE!");

	private final static Label websiteLabelWarning = new Label("Website URLs must begin with https:// or http:// or ftp:// or file:// or www. to be valid");
	private final static Label nonExecutable = new Label("Please select a program executable!");
	private final static Label fileAlreadyExistsWarning = new Label("File already exists, please select a different name.");
	private final static String regex = "^(((https?|ftp|file)://)|(www.))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private final static String OS = System.getProperty("os.name").toLowerCase();

	public static void main(String[] args) {
		launch(args);
	}
	//starting point for the application
	//this is where we put the code for the user interface
	@Override
	public void start(Stage stage) throws IOException {	
		scriptSites = new ArrayList<Website>();
		scriptFiles = new ArrayList<App>();

		websiteListPane = new FlowPane();
		fileListPane = new FlowPane();

		websiteLabelWarning.setWrapText(true);
		websiteLabelWarning.setPrefWidth(375);

		websiteURL = new TextField();
		websiteURL.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addWebsiteButton.fire();
			}
		});

		filePath = new TextField();
		filePath.setEditable(false);
		filePath.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addFileButton.fire();
			}
		});

		websiteLabelsListView = new ListView<String>();
		fileLabelsListView = new ListView<String>();


		websiteLabelsListView.setPrefSize(250, 150);
		websiteLabelsListView.setEditable(false);
		websiteLabelsListView.setStyle("-fx-font-weight: bold");

		fileLabelsListView.setPrefSize(250, 150);
		fileLabelsListView.setEditable(false);
		fileLabelsListView.setStyle("-fx-font-weight: bold");


		websiteListPane.getChildren().add(websiteLabelsListView);
		websiteListPane.setPadding(new Insets(20, 20, 20, 0));

		fileListPane.getChildren().add(fileLabelsListView);
		fileListPane.setPadding(new Insets(20, 20, 20, 0));

		//The stage is the top-level container
		stage.setTitle("Easy-Script-Generator");
		BorderPane appLayout = new BorderPane();
		BorderPane componentLayout = new BorderPane();
		componentLayout.setPadding(new Insets(20,0,20,20));

		tabPane = new TabPane();
		createTab = new Tab();
		createTab.setText("Create New Script");
		tabPane.getTabs().add(createTab);
		Tab updateTab = new Tab();
		updateTab.setText("Update Old Script");
		tabPane.getTabs().add(updateTab);
		appLayout.setTop(tabPane);

		updateTab.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event t) {

			}
		});

		websiteURLPane = new FlowPane();
		websiteURLPane.setHgap(10);
		Label urlLabel = new Label("Website URL");

		filePathPane = new FlowPane();
		filePathPane.setHgap(10);
		Label pathLabel = new Label("File Location");

		websiteURLPane.getChildren().add(urlLabel);
		websiteURLPane.getChildren().add(websiteURL);

		filePathPane.getChildren().add(pathLabel);
		filePathPane.getChildren().add(filePath);


		browseFilesButton.setOnAction(event -> {
			browseFiles();
		});

		addFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				addFile();
			}
		});

		removeFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				removeFile();
			}
		});

		clearWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				clearWebsites();
			}
		});

		clearFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				clearFiles();
			}
		});

		updateFileLVButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				updateFile();
			}
		});

		updateFilePathButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFilePath();
			}
		});


		filePathPane.getChildren().add(browseFilesButton);

		addFileButton.setManaged(false);
		addFileButton.setVisible(false);
		filePathPane.getChildren().add(addFileButton);


		removeWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				removeWebsite();
			}
		});


		addWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addWebsite();	
			}
		});

		websiteURLPane.getChildren().add(addWebsiteButton);

		updateWebsiteLVButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				updateWebsite();
			}
		});

		updateWebsiteURLButton.setVisible(false);
		updateWebsiteURLButton.setManaged(false);
		websiteURLPane.getChildren().add(updateWebsiteURLButton);

		updateFilePathButton.setVisible(false);
		updateFilePathButton.setManaged(false);
		filePathPane.getChildren().add(updateFilePathButton);

		websiteLabelWarning.setTextFill(Color.RED);
		websiteLabelWarning.setManaged(false);
		websiteLabelWarning.setVisible(false);
		websiteURLPane.getChildren().add(websiteLabelWarning);

		nonExecutable.setTextFill(Color.RED);
		nonExecutable.setManaged(false);
		nonExecutable.setVisible(false);
		filePathPane.getChildren().add(nonExecutable);

		fileAlreadyExistsWarning.setTextFill(Color.RED);
		fileAlreadyExistsWarning.setVisible(false);
		fileAlreadyExistsWarning.setManaged(false);

		removeWebsiteButton.setVisible(false);
		updateWebsiteLVButton.setVisible(false);
		clearWebsiteButton.setVisible(false);

		removeFileButton.setVisible(false);
		updateFileLVButton.setVisible(false);
		clearFileButton.setVisible(false);

		BorderPane websiteOptionsPane = new BorderPane();
		websiteOptionsPane.setTop(removeWebsiteButton);
		websiteOptionsPane.setCenter(updateWebsiteLVButton);
		websiteOptionsPane.setBottom(clearWebsiteButton);
		BorderPane.setAlignment(removeWebsiteButton, Pos.TOP_LEFT);
		BorderPane.setAlignment(updateWebsiteLVButton, Pos.CENTER_LEFT);
		BorderPane.setAlignment(clearWebsiteButton, Pos.BOTTOM_LEFT);
		websiteOptionsPane.setPadding(new Insets(0, 0, 0, 10));
		websiteListPane.getChildren().add(websiteOptionsPane);

		BorderPane fileOptionsPane = new BorderPane();
		fileOptionsPane.setTop(removeFileButton);
		fileOptionsPane.setCenter(updateFileLVButton);
		fileOptionsPane.setBottom(clearFileButton);
		BorderPane.setAlignment(removeFileButton, Pos.TOP_LEFT);
		BorderPane.setAlignment(updateFileLVButton, Pos.CENTER_LEFT);
		BorderPane.setAlignment(clearFileButton, Pos.BOTTOM_LEFT);
		fileOptionsPane.setPadding(new Insets(0, 0, 0, 10));
		fileListPane.getChildren().add(fileOptionsPane);


		websiteURLPane.getChildren().add(websiteListPane);
		filePathPane.getChildren().add(fileListPane);

		componentLayout.setTop(websiteURLPane);
		componentLayout.setCenter(filePathPane);

		FlowPane bottomPane = new FlowPane();
		bottomPane.getChildren().add(createScriptButton);
		componentLayout.setBottom(bottomPane);

		createScriptButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (scriptSites.isEmpty() == false || scriptFiles.isEmpty() == false) {
					final Stage dialog = new Stage();
					dialog.setTitle("Script Name Confirmation");
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.initOwner(primaryStage);
					BorderPane popupPane = new BorderPane();
					Scene dialogScene = new Scene(popupPane, 350, 200);
					dialog.setScene(dialogScene);
					dialog.show();

					scriptName = new TextField();
					scriptName.setPrefWidth(250);
					scriptName.setFont(new Font("Arial", 30));
					scriptName.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							popupCreateButton.fire();
						}
					});
					Label scriptNameLabel = new Label("Pick a name for your generated script!");
					scriptNameLabel.setFont(new Font("Arial", 20));
					scriptNameLabel.setPadding(new Insets(0, 0, 10, 0));
					FlowPane scriptPane = new FlowPane();
					scriptPane.getChildren().addAll(scriptNameLabel);
					scriptPane.getChildren().addAll(scriptName);
					scriptPane.setAlignment(Pos.CENTER);
					scriptPane.setPadding(new Insets(10, 0, 0, 0));
					popupPane.setTop(scriptPane);

					popupCreateButton.setFont(new Font("Arial", 50));

					FlowPane bottomPane = new FlowPane();
					FlowPane centerPane = new FlowPane();
					centerPane.setAlignment(Pos.CENTER);

					if (isWindows()) {
						dialogScene.getStylesheets().add(getClass().getResource("listStyles.css").toExternalForm());
					}

					popupCreateButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (scriptName != null) {

								try {
									createScript();
									clearWebsites();
									clearFiles();
									dialog.hide();

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					});



					centerPane.getChildren().add(popupCreateButton);
					bottomPane.getChildren().add(fileAlreadyExistsWarning);

					fileAlreadyExistsWarning.setVisible(false);
					fileAlreadyExistsWarning.setManaged(false);

					popupPane.setCenter(centerPane);
					popupPane.setBottom(bottomPane);
				}
			}
		});

		websiteLabelsListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateWebsiteURLButton.setVisible(false);
				updateWebsiteURLButton.setManaged(false);
				addWebsiteButton.setVisible(true);
				addWebsiteButton.setManaged(true);
				websiteURL.setText(null);
			}
		});

		appLayout.setCenter(componentLayout);

		if (isMac()) {
			appScene = new Scene(appLayout, 475, 550);
			//AquaFx.style();

		} else if (isWindows()) {
			appScene = new Scene(appLayout,400,500);
			appScene.getStylesheets().add(getClass().getResource("listStyles.css").toExternalForm());
		}


		appLayout.prefHeightProperty().bind(appScene.heightProperty());
		appLayout.prefWidthProperty().bind(appScene.widthProperty());

		stage.setScene(appScene);
		stage.show();
		stage.setResizable(false);
		primaryStage = stage;
	}

	public static void addWebsite() {
		String websiteName = websiteURL.getText().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
		Website website = new Website(websiteURL.getText(), websiteName);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(website.getURL());	
		if (website.getURL() != null && m.find()) {
			websiteLabelWarning.setManaged(false);
			websiteLabelWarning.setVisible(false);
			removeWebsiteButton.setVisible(true);
			updateWebsiteLVButton.setVisible(true);
			clearWebsiteButton.setVisible(true);
			scriptSites.add(website);
			websiteURL.clear();
			websiteLabelsListView.getItems().addAll(website.getLabel());
			websiteLabelsListView.getSelectionModel().select(website.getLabel());
			websiteURL.requestFocus();
		}
		else {
			websiteLabelWarning.setManaged(true);
			websiteLabelWarning.setVisible(true);
		}
	}

	public static void removeWebsite() {
		String websiteName = websiteLabelsListView.getSelectionModel().getSelectedItem();
		int selectedIndex = websiteLabelsListView.getSelectionModel().getSelectedIndex();
		if (websiteName != null) {
			scriptSites.remove(selectedIndex);
			websiteLabelsListView.getItems().remove(selectedIndex);
		}
		if (websiteLabelsListView.getItems().isEmpty()) {
			removeWebsiteButton.setVisible(false);
			clearWebsiteButton.setVisible(false);
			updateWebsiteLVButton.setVisible(false);
		}
	}

	public static void updateWebsite() {
		String websiteToUpdateName = websiteLabelsListView.getSelectionModel().getSelectedItem();
		if (websiteToUpdateName != null) {
			addWebsiteButton.setVisible(false);
			addWebsiteButton.setManaged(false);
			updateWebsiteURLButton.setVisible(true);
			updateWebsiteURLButton.setManaged(true);
			int selectedIndex = websiteLabelsListView.getSelectionModel().getSelectedIndex();
			websiteURL.setText(scriptSites.get(selectedIndex).getURL());

			updateWebsiteURLButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String websiteToAddName = websiteURL.getText().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
					Website tempWebsite = new Website(websiteURL.getText(), websiteToAddName);
					websiteURL.clear();
					websiteLabelsListView.getItems().remove(selectedIndex);
					scriptSites.remove(selectedIndex);
					scriptSites.add(selectedIndex, tempWebsite);
					websiteLabelsListView.getItems().add(selectedIndex, tempWebsite.getLabel());
					websiteLabelsListView.getSelectionModel().select(selectedIndex);
					updateWebsiteURLButton.setVisible(false);
					updateWebsiteURLButton.setManaged(false);
					addWebsiteButton.setManaged(true);
					addWebsiteButton.setVisible(true);
				}
			});
		}
	}

	public static void clearWebsites() {
		websiteLabelsListView.getItems().clear();
		scriptSites.clear();
		removeWebsiteButton.setVisible(false);
		updateWebsiteLVButton.setVisible(false);
		clearWebsiteButton.setVisible(false);
		websiteURL.clear();
	}

	public static void addFile() {
		scriptFiles.add(new App(fileChosen.getAbsolutePath(), fileChosen.getName()));
		fileLabelsListView.getItems().add(fileChosen.getName());
		fileLabelsListView.getSelectionModel().select(fileChosen.getName());
		fileLabelsListView.requestFocus();
		addFileButton.setManaged(false);
		addFileButton.setVisible(false);
		removeFileButton.setVisible(true);
		updateFileLVButton.setVisible(true);
		clearFileButton.setVisible(true);
		filePath.clear();
	}

	public static void removeFile() {
		String selectedItem = fileLabelsListView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			int selectedIndex = fileLabelsListView.getSelectionModel().getSelectedIndex();
			scriptFiles.remove(selectedIndex);
			fileLabelsListView.getItems().remove(selectedIndex);
		}
		if (fileLabelsListView.getItems().isEmpty()) {
			removeFileButton.setVisible(false);
			clearFileButton.setVisible(false);
			updateFileLVButton.setVisible(false);
		}
	}

	public static void updateFile() {
		String fileToUpdateName = fileLabelsListView.getSelectionModel().getSelectedItem();
		if (fileToUpdateName != null) {
			addFileButton.setVisible(false);
			addFileButton.setManaged(false);
			updateFilePathButton.setVisible(true);
			updateFilePathButton.setManaged(true);
			FileChooser chooser = new FileChooser();
			File file = chooser.showOpenDialog(primaryStage);
			if (file != null) {
				nonExecutable.setManaged(false);
				nonExecutable.setVisible(false);
				filePath.setText(file.getName());
				fileChosen = file;
			} else {
				nonExecutable.setManaged(true);
				nonExecutable.setVisible(true);
			}
		}
	}

	public static void browseFiles() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(primaryStage);
		if (file != null) {
			nonExecutable.setManaged(false);
			nonExecutable.setVisible(false);
			filePath.setText(file.getName());
			fileChosen = file;
			addFileButton.setManaged(true);
			addFileButton.setVisible(true);
			filePath.requestFocus();
		} else {
			nonExecutable.setManaged(true);
			nonExecutable.setVisible(true);
		}
	}

	public static void updateFilePath() {
		int selectedIndex = fileLabelsListView.getSelectionModel().getSelectedIndex();
		App tempApp = new App(fileChosen.getAbsolutePath(), fileChosen.getName());
		filePath.clear();
		fileLabelsListView.getItems().remove(selectedIndex);
		scriptFiles.remove(selectedIndex);
		scriptFiles.add(selectedIndex, tempApp);
		fileLabelsListView.getItems().add(selectedIndex, tempApp.getLabel());
		fileLabelsListView.getSelectionModel().select(selectedIndex);

		fileChosen = null;
		updateFilePathButton.setVisible(false);
		updateFilePathButton.setManaged(false);
	}

	public static void clearFiles() {
		fileLabelsListView.getItems().clear();
		scriptFiles.clear();
		removeFileButton.setVisible(false);
		updateFileLVButton.setVisible(false);
		updateFilePathButton.setVisible(false);
		addFileButton.setVisible(false);
		clearFileButton.setVisible(false);
		filePath.clear();
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static void createScript() throws IOException {

		if (isWindows()) {
			File file=new File(System.getProperty("user.home") + "\\Desktop\\" + scriptName.getText() + ".bat");
			if (file.isFile()) {
				fileAlreadyExistsWarning.setVisible(true);
				fileAlreadyExistsWarning.setManaged(true);
			}
			else {
				file.setReadable(false,false);
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
						dos.writeBytes(newLine);
					}
				}

				for (App currApp: scriptFiles) {
					dos.writeBytes("\"" + currApp.getPath() + "\"");
					dos.writeBytes(newLine);
				}
				dos.close();
				fos.close();
			}
		} else if (isMac()) {
			File file = new File(System.getProperty("user.home") + "/Desktop/" + scriptName.getText() + ".command"); 
			FileOutputStream fos=new FileOutputStream(file); 
			DataOutputStream dos=new DataOutputStream(fos); 
			String newLine = System.getProperty("line.separator");
			dos.writeBytes("#!/bin/bash");
			dos.writeBytes(newLine);
			for (Website website: scriptSites) {
				if (website.getURL().startsWith("www.") || website.getURL().startsWith("https://") == false && website.getURL().startsWith("http://") == false) {
					website = new Website("https://" + website.getURL(), website.getLabel());
				}
				if (scriptSites.get(0) == website) {
					dos.writeBytes("open -na 'Google Chrome' --args --new-window " + website.getURL()); 
					dos.writeBytes(newLine);
					file.setReadOnly();
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
				else if (scriptSites.get(scriptSites.size() - 1) == website) {
					dos.writeBytes("open -na 'Google Chrome' " + website.getURL()); 
					dos.writeBytes(newLine);
				}
				else {
					dos.writeBytes("open -na 'Google Chrome' " + website.getURL()); 
					dos.writeBytes(newLine);
				}
			}
			for (App currApp: scriptFiles) {
				dos.writeBytes("open '" + currApp.getPath() + "'"); 
				dos.writeBytes(newLine);
			}
			file.setReadOnly();
			dos.close();
			fos.close();
			file.setExecutable(true);
		} else {
			System.out.println("Your OS is not supported!!");
		}
	}

	public static void editScript(ArrayList<Website> websites, ArrayList<App> files) throws IOException { 
		if (websites.isEmpty() == false ) {
			for (Website currWebsite: websites) {
				websiteLabelsListView.getItems().add(currWebsite.getLabel());
			}
			scriptSites = websites;
			scriptFiles = files;

		}
	}

	public static void readScript(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String currLine = sc.nextLine();
		}
		sc.close();
	}
}