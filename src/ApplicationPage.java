
//Imports are listed in full to show what's being used
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ApplicationPage {

	private Script script;

	private TextField websiteURL;
	private TextField filePath;

	private File fileChosen;
	private File fileToUpdate;

	private ComboBox<String> websiteBrowsers;
	private String browserChosen;

	/*private static TabPane tabPane;
	private static Tab createTab;
	private static Tab updateTab;*/

	private FlowPane websiteListPane;
	private FlowPane fileListPane;
	private FlowPane websiteURLPane;
	private FlowPane filePathPane;

	private ListView<String> websiteLabelsListView;
	private ListView<String> fileLabelsListView;

	protected Scene appScene;
	protected Stage primaryStage;
	protected BorderPane appLayout;

	final Button editOldScriptButton = new Button("Import Shortcut...");

	private final Button removeWebsiteButton = new Button("Remove Website");
	private final Button updateWebsiteLVButton = new Button("Update Website");
	private final Button addWebsiteButton = new Button("Add website!");
	private final Button updateWebsiteURLButton = new Button("Update Website!");
	private final Button clearWebsiteButton = new Button("Clear All Websites");
	private final Button saveScriptButton = new Button("Save Shortcut As...");

	private final Button browseFilesButton = new Button("Browse files...");
	private final Button addFileButton = new Button("Add File!");
	private final Button removeFileButton = new Button("Remove File");
	private final Button updateFileLVButton = new Button("Update File");
	private final Button clearFileButton = new Button("Clear All Files");
	private final Button updateFilePathButton = new Button("Update!");

	private final Button updateScriptButton = new Button(("Update Script!"));

	private final Label websiteLabelWarning = new Label("Invalid URL, please check that the url is correct.");
	private final Label nonExecutableWarning = new Label("Please select a file or application!");
	private final Label selectFileNameWarning = new Label("Please enter a file name to save the shortcut.");
	private final Label noFileSelectedWarning = new Label("Please select an EZScripts file to import.");
	private final Label noSitesOrFilesWarning = new Label("There is nothing to save, please add at least one website or file.");

	private final Label saveScriptSuccessMessage = new Label("Successfully saved shortcut!");
	private final Label updateScriptSuccessMessage = new Label("Successfully updated shortcut!");

	private FadeTransition fadeOutWLW;
	private FadeTransition fadeOutNEW;
	private FadeTransition fadeOutSFNW;
	private FadeTransition fadeOutNFSW;
	private FadeTransition fadeOutNSOFW;
	private FadeTransition fadeOutSSSM;
	private FadeTransition fadeOutUSSM;

	private final static String regex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
	private final static String OS = System.getProperty("os.name").toLowerCase();
	private final static String batchWebsiteSection = new String("::Website Section");
	private final static String batchFileSection = new String("::File Section");
	private final static String bashWebsiteSection = new String("#Website Section");
	private final static String bashFileSection = new String("#File Section");

	public ApplicationPage(Script script) {
		this.script = script;
		this.primaryStage = ApplicationWindow.primaryStage;

		/**
		 * Initializes website list and file list panes.
		 */
		websiteListPane = new FlowPane();
		fileListPane = new FlowPane();

		/**
		 * Wraps the text of the website warning (when an incorrect format of a website is input) and sets the width.
		 */
		websiteLabelWarning.setWrapText(true);
		websiteLabelWarning.setPrefWidth(375);

		/**
		 * Initializes the url textfield.
		 */
		websiteURL = new TextField();

		/**
		 * When a user clicks Enter after typing a website in the text box, it fires the add website button.
		 */
		websiteURL.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (addWebsiteButton.isVisible()) {
				addWebsiteButton.fire();
				}
				else if (updateWebsiteURLButton.isVisible()) {
					updateWebsiteURLButton.fire();
				}
			}
		});

		/**
		 * Initializes the file path textfield and makes it not editable.
		 */
		filePath = new TextField();
		filePath.setEditable(false);

		/**
		 * When a user clicks Enter after picking a file in the file browse, it fires the add file button.
		 */
		filePath.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (addFileButton.isVisible()) {
					addFileButton.fire();
					}
					else if (updateFilePathButton.isVisible()) {
						updateFilePathButton.fire();
					}
			}
		});

		/**
		 * Initializes the website browser combo box and adds different browsers (some dependent on OS).
		 */
		websiteBrowsers = new ComboBox<String>();
		ArrayList<String> browserOptions = new ArrayList<String>();
		/*if (isMac()) {
			browserOptions.add("Safari");
		}*/
		browserOptions.add("Google Chrome");
		if (isWindows()) {
			browserOptions.add("FireFox");
			//browserOptions.add("Internet Explorer");
		}
		websiteBrowsers.getItems().addAll(browserOptions);
		websiteBrowsers.getSelectionModel().selectFirst();


		/**
		 * Initializes the website list view and file list view.
		 */
		websiteLabelsListView = new ListView<String>();
		fileLabelsListView = new ListView<String>();


		/**
		 * Sets properties of website list view.
		 */
		websiteLabelsListView.setPrefSize(250, 150);
		websiteLabelsListView.setEditable(false);
		websiteLabelsListView.setStyle("-fx-font-weight: bold");

		/**
		 * Sets properties of file list view.
		 */
		fileLabelsListView.setPrefSize(250, 150);
		fileLabelsListView.setEditable(false);
		fileLabelsListView.setStyle("-fx-font-weight: bold");

		/**
		 * Sets cell factory of both website list view and file list view to custom styles.
		 */
		websiteLabelsListView.setCellFactory(param -> new WebsiteCell());
		fileLabelsListView.setCellFactory(param -> new FileCell());

		/**
		 * Adds website list view to the website list pane.
		 */
		websiteListPane.getChildren().add(websiteLabelsListView);
		websiteListPane.setPadding(new Insets(20, 20, 20, 0));

		/**
		 * Adds file list view to the file list pane.
		 */
		fileListPane.getChildren().add(fileLabelsListView);
		fileListPane.setPadding(new Insets(20, 20, 20, 0));

		/**
		 * Adds main panes to the application.
		 */
		appLayout = new BorderPane();
		BorderPane componentLayout = new BorderPane();
		componentLayout.setPadding(new Insets(20,0,20,20));


		/**
		 * Initializes the website url pane.
		 */
		websiteURLPane = new FlowPane();
		websiteURLPane.setHgap(10);
		Label urlLabel = new Label("Website URL");

		/**
		 * Initializes the file path pane.
		 */
		filePathPane = new FlowPane();
		filePathPane.setHgap(10);
		Label pathLabel = new Label("File Location");

		/**
		 * Adds the website url components to the website url pane, and adds the file path components to the file path pane.
		 */
		websiteURLPane.getChildren().add(urlLabel);
		websiteURLPane.getChildren().add(websiteURL);
		filePathPane.getChildren().add(pathLabel);
		filePathPane.getChildren().add(filePath);


		/**
		 * When the browse files button is pressed, it fires the browse files method.
		 */
		browseFilesButton.setOnAction(event -> {
			browseFiles();
		});

		/**
		 * When the add file button is pressed, it fires the add file method.
		 */
		addFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				addFile();
			}
		});

		/**
		 * When the remove file button is pressed, it fires the remove file method.
		 */
		removeFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				removeFile();
			}
		});

		/**
		 * When the clear website button is pressed, it fires the clear website method.
		 */
		clearWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				clearWebsites();
			}
		});

		/**
		 * When the clear file button is pressed, it fires the clear file method.
		 */
		clearFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				clearFiles();
			}
		});

		/**
		 * When the update file list view button is pressed, it fires the update file method.
		 */
		updateFileLVButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				removeFileButton.setDisable(true);
				clearFileButton.setDisable(true);
				updateFile();
				filePath.requestFocus();
			}
		});

		/**
		 * When the update file path button is pressed, it fires the update file path method.
		 */
		updateFilePathButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFilePath();
				removeFileButton.setDisable(false);
				clearFileButton.setDisable(false);
			}
		});


		/**
		 * Adds the browse files button and add file button to the file path pane.
		 */
		filePathPane.getChildren().add(browseFilesButton);
		addFileButton.setManaged(false);
		addFileButton.setVisible(false);
		filePathPane.getChildren().add(addFileButton);

		/**
		 * When the remove website button is pressed, it fires the remove website method.
		 */
		removeWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				removeWebsite();
			}
		});


		/**
		 * When the add website button is pressed, it fires the add website method and checks the url against the regex for a url.
		 */
		addWebsiteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (websiteURL.getText() != null) {
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(websiteURL.getText());	
					if (m.find()) {
						String websiteName = websiteURL.getText().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
						Website website = new Website(websiteURL.getText(), websiteName);
						addWebsite(website);
					}
					else {
						websiteLabelWarning.setVisible(true);
						fadeOutWLW.setNode(websiteLabelWarning);
						fadeOutWLW.setFromValue(0.0);
						fadeOutWLW.setToValue(1.0);
						fadeOutWLW.setCycleCount(2);
						fadeOutWLW.setAutoReverse(true);
						fadeOutWLW.playFromStart();
						fadeOutWLW.setOnFinished(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								websiteLabelWarning.setManaged(false);
							}
						});
					}
				}
				else {
					websiteLabelWarning.setVisible(true);
					fadeOutWLW.setNode(websiteLabelWarning);
					fadeOutWLW.setFromValue(0.0);
					fadeOutWLW.setToValue(1.0);
					fadeOutWLW.setCycleCount(2);
					fadeOutWLW.setAutoReverse(true);
					fadeOutWLW.playFromStart();
					fadeOutWLW.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent actionEvent) {
							websiteLabelWarning.setManaged(false);
						}
					});
				}
			}
		});

		/**
		 * Adds the add website button to the website url pane.
		 */
		websiteURLPane.getChildren().add(addWebsiteButton);

		/**
		 * Adds the update website url button to the website url pane.
		 */
		updateWebsiteURLButton.setVisible(false);
		updateWebsiteURLButton.setManaged(false);
		websiteURLPane.getChildren().add(updateWebsiteURLButton);

		/**
		 * Adds the website browsers combo box to the website url pane.
		 */
		websiteURLPane.getChildren().add(websiteBrowsers);

		/**
		 * When the update website list view button is pressed, it fires the update website method.
		 */
		updateWebsiteLVButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				clearWebsiteButton.setDisable(true);
				removeWebsiteButton.setDisable(true);
				updateWebsite();
				websiteURL.requestFocus();
			}
		});

		/**
		 * When the update script button is pressed, it fires the update script method
		 */
		updateScriptButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				updateScript();
				noSitesOrFilesWarning.setManaged(false);
				noFileSelectedWarning.setManaged(false);
				selectFileNameWarning.setManaged(false);
				saveScriptSuccessMessage.setManaged(false);
				updateScriptSuccessMessage.setVisible(true);
				updateScriptSuccessMessage.setManaged(true);
				fadeOutUSSM.setNode(updateScriptSuccessMessage);
				fadeOutUSSM.setFromValue(0.0);
				fadeOutUSSM.setToValue(1.0);
				fadeOutUSSM.setCycleCount(2);
				fadeOutUSSM.setAutoReverse(true);
				fadeOutUSSM.playFromStart();
				fadeOutUSSM.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						updateScriptSuccessMessage.setManaged(false);
					}
				});
			}
		});


		editOldScriptButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				FileChooser chooser = new FileChooser();
				chooser.setTitle("Select shortcut to edit");
				if (isWindows()) {
					ExtensionFilter filter = new ExtensionFilter("Script Files", "*.bat");
					chooser.getExtensionFilters().add(filter);
				}
				else if (isMac()) {
					ExtensionFilter filter = new ExtensionFilter("Script Files", "*.command");
					chooser.getExtensionFilters().add(filter);
				}
				chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop/"));
				File file = chooser.showOpenDialog(primaryStage);
				if (file != null) {
					fileToUpdate = file;
					try {
						clearWebsites();
						clearFiles();
						readScript(file);
						if (isWindows()) {
							websiteBrowsers.getSelectionModel().select(browserChosen);
						}
						ApplicationWindow.tabPane.getSelectionModel().getSelectedItem().setText(file.getName());
					} catch (FileNotFoundException e) {

					}
				}
				else {
					selectFileNameWarning.setManaged(false);
					noSitesOrFilesWarning.setManaged(false);
					saveScriptSuccessMessage.setManaged(false);
					updateScriptSuccessMessage.setManaged(false);
					noFileSelectedWarning.setVisible(true);
					noFileSelectedWarning.setManaged(true);
					fadeOutNFSW.setNode(noFileSelectedWarning);
					fadeOutNFSW.setFromValue(0.0);
					fadeOutNFSW.setToValue(1.0);
					fadeOutNFSW.setCycleCount(2);
					fadeOutNFSW.setAutoReverse(true);
					fadeOutNFSW.playFromStart();
				}
			}
		});

		/**
		 * Adds the update file path button to the file path pane.
		 */
		updateFilePathButton.setVisible(false);
		updateFilePathButton.setManaged(false);
		filePathPane.getChildren().add(updateFilePathButton);

		/**
		 * Adds the website label warning for wrong format to the website url pane (invisible until the condition is met).
		 */
		websiteLabelWarning.setTextFill(Color.RED);
		websiteLabelWarning.setManaged(true);
		websiteLabelWarning.setVisible(false);
		websiteURLPane.getChildren().add(websiteLabelWarning);

		/**
		 * Adds the non executable warning to the file path pane.
		 */
		nonExecutableWarning.setTextFill(Color.RED);
		nonExecutableWarning.setManaged(true);
		nonExecutableWarning.setVisible(false);
		filePathPane.getChildren().add(nonExecutableWarning);

		selectFileNameWarning.setTextFill(Color.RED);
		selectFileNameWarning.setVisible(false);
		selectFileNameWarning.setManaged(true);

		noFileSelectedWarning.setTextFill(Color.RED);
		noFileSelectedWarning.setVisible(false);
		noFileSelectedWarning.setManaged(false);

		noSitesOrFilesWarning.setTextFill(Color.RED);
		noSitesOrFilesWarning.setVisible(false);
		noSitesOrFilesWarning.setManaged(false);

		saveScriptSuccessMessage.setTextFill(Color.GREEN);
		saveScriptSuccessMessage.setVisible(false);
		saveScriptSuccessMessage.setManaged(false);

		updateScriptSuccessMessage.setTextFill(Color.GREEN);
		updateScriptSuccessMessage.setVisible(false);
		updateScriptSuccessMessage.setManaged(false);

		fadeOutWLW = new FadeTransition(
				Duration.millis(4000)
				);

		fadeOutWLW = new FadeTransition(
				Duration.millis(4000)
				);

		fadeOutNEW = new FadeTransition(
				Duration.millis(4000)
				);

		fadeOutSFNW = new FadeTransition(
				Duration.millis(4000)
				);

		fadeOutNFSW = new FadeTransition(
				Duration.millis(4000)
				);

		fadeOutNSOFW = new FadeTransition(
				Duration.millis(4000)
				);

		fadeOutSSSM = new FadeTransition(
				Duration.millis(4000)
				);

		fadeOutUSSM = new FadeTransition(
				Duration.millis(4000)
				);


		updateScriptButton.setVisible(false);
		updateScriptButton.setManaged(false);

		/**
		 * Sets various components invisible (until conditions are met).
		 */
		removeWebsiteButton.setVisible(false);
		updateWebsiteLVButton.setVisible(false);
		clearWebsiteButton.setVisible(false);

		/**
		 * Sets various components invisible (until conditions are met).
		 */
		removeFileButton.setVisible(false);
		updateFileLVButton.setVisible(false);
		clearFileButton.setVisible(false);

		/**
		 * Adds website buttons to the website options pane.
		 */
		BorderPane websiteOptionsPane = new BorderPane();
		websiteOptionsPane.setTop(removeWebsiteButton);
		websiteOptionsPane.setCenter(updateWebsiteLVButton);
		websiteOptionsPane.setBottom(clearWebsiteButton);
		BorderPane.setAlignment(removeWebsiteButton, Pos.TOP_LEFT);
		BorderPane.setAlignment(updateWebsiteLVButton, Pos.CENTER_LEFT);
		BorderPane.setAlignment(clearWebsiteButton, Pos.BOTTOM_LEFT);
		websiteOptionsPane.setPadding(new Insets(0, 0, 0, 10));
		websiteListPane.getChildren().add(websiteOptionsPane);

		/**
		 * Adds file buttons to the file options pane.
		 */
		BorderPane fileOptionsPane = new BorderPane();
		fileOptionsPane.setTop(removeFileButton);
		fileOptionsPane.setCenter(updateFileLVButton);
		fileOptionsPane.setBottom(clearFileButton);
		BorderPane.setAlignment(removeFileButton, Pos.TOP_LEFT);
		BorderPane.setAlignment(updateFileLVButton, Pos.CENTER_LEFT);
		BorderPane.setAlignment(clearFileButton, Pos.BOTTOM_LEFT);
		fileOptionsPane.setPadding(new Insets(0, 0, 0, 10));
		fileListPane.getChildren().add(fileOptionsPane);


		/**
		 * Adds website list pane to the website url pane, and adds the file list pane to the file path pane.
		 */
		websiteURLPane.getChildren().add(websiteListPane);
		filePathPane.getChildren().add(fileListPane);

		/**
		 * Positions the two panes in the main application pane.
		 */
		componentLayout.setTop(websiteURLPane);
		componentLayout.setCenter(filePathPane);

		/**
		 * Adds create script button and update script button to the bottom pane.
		 */
		FlowPane bottomPane = new FlowPane();
		BorderPane scriptButtonPane = new BorderPane();
		FlowPane saveAndUpdatePane = new FlowPane();
		saveAndUpdatePane.getChildren().add(saveScriptButton);
		saveAndUpdatePane.getChildren().add(updateScriptButton);
		if (isWindows()) {
			saveAndUpdatePane.prefWrapLengthProperty().set(325);
		}
		else if (isMac()) {
			saveAndUpdatePane.prefWrapLengthProperty().set(420);
		}

		scriptButtonPane.setLeft(saveAndUpdatePane);
		scriptButtonPane.setRight(editOldScriptButton);


		bottomPane.getChildren().add(scriptButtonPane);

		FlowPane warningPane = new FlowPane();
		warningPane.getChildren().add(selectFileNameWarning);
		warningPane.getChildren().add(noFileSelectedWarning);
		warningPane.getChildren().add(noSitesOrFilesWarning);
		warningPane.getChildren().add(saveScriptSuccessMessage);
		warningPane.getChildren().add(updateScriptSuccessMessage);
		bottomPane.getChildren().add(warningPane);

		/**
		 * Makes update script button invisible (until conditions are met) and adds the bottom pane to the main application pane.
		 */
		componentLayout.setBottom(bottomPane);

		/**
		 * When the create script button is pressed, the process of creating the script file is triggered, and it brings up the popup to let the user name the script and confirm its' creation.
		 */
		saveScriptButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (script.getWebsites().isEmpty() == false || script.getFiles().isEmpty() == false) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop/"));
					fileChooser.setInitialFileName(ApplicationWindow.tabPane.getSelectionModel().getSelectedItem().getText());
					fileChooser.setTitle("Save Shortcut As...");
					if (isWindows()) {
						fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Batch File(*.bat)", "*.bat"));
					}
					else if (isMac()) {
						fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Command File(*.command)", "*.command"));
					}
					File file = fileChooser.showSaveDialog(primaryStage);
					try {
						if (file == null) {
							noSitesOrFilesWarning.setManaged(false);
							noFileSelectedWarning.setManaged(false);
							updateScriptSuccessMessage.setManaged(false);
							saveScriptSuccessMessage.setManaged(false);
							selectFileNameWarning.setVisible(true);
							selectFileNameWarning.setManaged(true);
							fadeOutSFNW.setNode(selectFileNameWarning);
							fadeOutSFNW.setFromValue(0.0);
							fadeOutSFNW.setToValue(1.0);
							fadeOutSFNW.setCycleCount(2);
							fadeOutSFNW.setAutoReverse(true);
							fadeOutSFNW.playFromStart();
						}
						else {
							createScript(file);
							ApplicationWindow.tabPane.getSelectionModel().getSelectedItem().setText(file.getName());
							noSitesOrFilesWarning.setManaged(false);
							noFileSelectedWarning.setManaged(false);
							selectFileNameWarning.setManaged(false);
							updateScriptSuccessMessage.setManaged(false);
							saveScriptSuccessMessage.setManaged(true);
							saveScriptSuccessMessage.setVisible(true);
							fadeOutSSSM.setNode(saveScriptSuccessMessage);
							fadeOutSSSM.setFromValue(0.0);
							fadeOutSSSM.setToValue(1.0);
							fadeOutSSSM.setCycleCount(2);
							fadeOutSSSM.setAutoReverse(true);
							fadeOutSSSM.playFromStart();
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					noFileSelectedWarning.setManaged(false);
					selectFileNameWarning.setManaged(false);
					updateScriptSuccessMessage.setManaged(false);
					saveScriptSuccessMessage.setManaged(false);
					noSitesOrFilesWarning.setVisible(true);
					noSitesOrFilesWarning.setManaged(true);
					fadeOutNSOFW.setNode(noSitesOrFilesWarning);
					fadeOutNSOFW.setFromValue(0.0);
					fadeOutNSOFW.setToValue(1.0);
					fadeOutNSOFW.setCycleCount(2);
					fadeOutNSOFW.setAutoReverse(true);
					fadeOutNSOFW.playFromStart();
				}
			}
		});

		/**
		 * 
		 */
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
			appScene = new Scene(appLayout, 575, 600);
			//AquaFx.style();

		} else if (isWindows()) {
			appScene = new Scene(appLayout,440,560);
			appScene.getStylesheets().add(getClass().getResource("windowsStyles.css").toExternalForm());
		}


		appLayout.setPrefHeight(575);
		appLayout.setPrefWidth(600);
		//appLayout.prefHeightProperty().bind(appScene.heightProperty());
		//appLayout.prefWidthProperty().bind(appScene.widthProperty());

		websiteURL.requestFocus();
	}

	private class WebsiteCell extends ListCell<String> {

		public WebsiteCell() {
			ListCell<String> thisCell = this;

			setContentDisplay(ContentDisplay.TEXT_ONLY);

			setOnDragDetected(event -> {
				if (getItem() == null) {
					return;
				}

				Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString(getItem());
				//dragboard.setDragView(null);
				dragboard.setContent(content);

				event.consume();
			});

			setOnDragOver(event -> {
				if (event.getGestureSource() != thisCell &&
						event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}

				event.consume();
			});

			setOnDragEntered(event -> {
				if (event.getGestureSource() != thisCell &&
						event.getDragboard().hasString()) {
					setOpacity(0.3);
				}
			});

			setOnDragExited(event -> {
				if (event.getGestureSource() != thisCell &&
						event.getDragboard().hasString()) {
					setOpacity(1);
				}
			});

			setOnDragDropped(event -> {
				if (getItem() == null) {
					return;
				}

				Dragboard db = event.getDragboard();
				boolean success = false;

				if (db.hasString()) {
					ObservableList<String> items = getListView().getItems();
					int draggedIdx = items.indexOf(db.getString());
					int thisIdx = items.indexOf(getItem());

					items.set(draggedIdx, getItem());
					items.set(thisIdx, db.getString());

					Website tempDragged = script.getWebsites().get(draggedIdx);
					Website tempDropped = script.getWebsites().get(thisIdx);

					script.getWebsites().set(draggedIdx, tempDropped);
					script.getWebsites().set(thisIdx, tempDragged);

					this.setText(items.get(draggedIdx));

					List<String> itemscopy = new ArrayList<>(getListView().getItems());
					getListView().getItems().setAll(itemscopy);

					success = true;
				}
				event.setDropCompleted(success);

				event.consume();
			});

			setOnDragDone(DragEvent::consume);
		}


		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty || item == null) {
				this.setText(null);
			} else {
				this.setText(item);
			}
		}
	}


	private class FileCell extends ListCell<String> {

		public FileCell() {
			ListCell<String> thisCell = this;

			setContentDisplay(ContentDisplay.TEXT_ONLY);

			setOnDragDetected(event -> {
				if (getItem() == null) {
					return;
				}

				Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString(getItem());
				dragboard.setContent(content);

				event.consume();
			});

			setOnDragOver(event -> {
				if (event.getGestureSource() != thisCell &&
						event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}

				event.consume();
			});

			setOnDragEntered(event -> {
				if (event.getGestureSource() != thisCell &&
						event.getDragboard().hasString()) {
					setOpacity(0.3);
				}
			});

			setOnDragExited(event -> {
				if (event.getGestureSource() != thisCell &&
						event.getDragboard().hasString()) {
					setOpacity(1);
				}
			});

			setOnDragDropped(event -> {
				if (getItem() == null) {
					return;
				}

				Dragboard db = event.getDragboard();
				boolean success = false;

				if (db.hasString()) {
					ObservableList<String> items = getListView().getItems();
					int draggedIdx = items.indexOf(db.getString());
					int thisIdx = items.indexOf(getItem());

					items.set(draggedIdx, getItem());
					items.set(thisIdx, db.getString());

					App tempDragged = script.getFiles().get(draggedIdx);
					App tempDropped = script.getFiles().get(thisIdx);

					script.getFiles().set(draggedIdx, tempDropped);
					script.getFiles().set(thisIdx, tempDragged);

					this.setText(items.get(draggedIdx));

					List<String> itemscopy = new ArrayList<>(getListView().getItems());
					getListView().getItems().setAll(itemscopy);

					success = true;
				}
				event.setDropCompleted(success);

				event.consume();
			});

			setOnDragDone(DragEvent::consume);
		}


		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty || item == null) {
				this.setText(null);
			} else {
				this.setText(item);
			}
		}
	}

	public void addWebsite(Website website) {
		removeWebsiteButton.setVisible(true);
		updateWebsiteLVButton.setVisible(true);
		clearWebsiteButton.setVisible(true);
		script.getWebsites().add(website);
		websiteURL.clear();
		websiteLabelsListView.getItems().addAll(website.getLabel());
		websiteLabelsListView.getSelectionModel().selectLast();
		websiteURL.requestFocus();
	}

	public void removeWebsite() {
		String websiteName = websiteLabelsListView.getSelectionModel().getSelectedItem();
		int selectedIndex = websiteLabelsListView.getSelectionModel().getSelectedIndex();
		if (websiteName != null) {
			script.getWebsites().remove(selectedIndex);
			websiteLabelsListView.getItems().remove(selectedIndex);
		}
		if (websiteLabelsListView.getItems().isEmpty()) {
			removeWebsiteButton.setVisible(false);
			clearWebsiteButton.setVisible(false);
			updateWebsiteLVButton.setVisible(false);
		}
	}

	public void updateWebsite() {
		String websiteToUpdateName = websiteLabelsListView.getSelectionModel().getSelectedItem();
		if (websiteToUpdateName != null) {
			addWebsiteButton.setVisible(false);
			addWebsiteButton.setManaged(false);
			updateWebsiteURLButton.setVisible(true);
			updateWebsiteURLButton.setManaged(true);
			int selectedIndex = websiteLabelsListView.getSelectionModel().getSelectedIndex();
			websiteURL.setText(script.getWebsites().get(selectedIndex).getURL());

			updateWebsiteURLButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String websiteToAddName = websiteURL.getText().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
					Website tempWebsite = new Website(websiteURL.getText(), websiteToAddName);
					websiteURL.clear();
					websiteLabelsListView.getItems().remove(selectedIndex);
					script.getWebsites().remove(selectedIndex);
					script.getWebsites().add(selectedIndex, tempWebsite);
					websiteLabelsListView.getItems().add(selectedIndex, tempWebsite.getLabel());
					websiteLabelsListView.getSelectionModel().select(selectedIndex);
					updateWebsiteURLButton.setVisible(false);
					updateWebsiteURLButton.setManaged(false);
					addWebsiteButton.setManaged(true);
					addWebsiteButton.setVisible(true);
					removeWebsiteButton.setDisable(false);
					clearWebsiteButton.setDisable(false);
				}
			});
		}
	}

	public void clearWebsites() {
		websiteLabelsListView.getItems().clear();
		script.getWebsites().clear();
		removeWebsiteButton.setVisible(false);
		updateWebsiteLVButton.setVisible(false);
		clearWebsiteButton.setVisible(false);
		websiteURL.clear();
	}

	public void addFile() {
		script.getFiles().add(new App(fileChosen.getAbsolutePath(), fileChosen.getName()));
		fileLabelsListView.getItems().add(fileChosen.getName());
		fileLabelsListView.getSelectionModel().selectLast();
		fileLabelsListView.requestFocus();
		addFileButton.setManaged(false);
		addFileButton.setVisible(false);
		removeFileButton.setVisible(true);
		updateFileLVButton.setVisible(true);
		clearFileButton.setVisible(true);
		filePath.requestFocus();
		filePath.clear();
	}

	public void removeFile() {
		String selectedItem = fileLabelsListView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			int selectedIndex = fileLabelsListView.getSelectionModel().getSelectedIndex();
			script.getFiles().remove(selectedIndex);
			fileLabelsListView.getItems().remove(selectedIndex);
		}
		if (fileLabelsListView.getItems().isEmpty()) {
			removeFileButton.setVisible(false);
			clearFileButton.setVisible(false);
			updateFileLVButton.setVisible(false);
		}
	}

	public void updateFile() {
		String fileToUpdateName = fileLabelsListView.getSelectionModel().getSelectedItem();
		if (fileToUpdateName != null) {
			addFileButton.setVisible(false);
			addFileButton.setManaged(false);

			FileChooser chooser = new FileChooser();
			chooser.setTitle("Update File");
			File file = chooser.showOpenDialog(primaryStage);
			if (file != null) {
				updateFilePathButton.setVisible(true);
				updateFilePathButton.setManaged(true);
				filePath.setText(file.getName());
				fileChosen = file;
			} else {
				nonExecutableWarning.setManaged(true);
				nonExecutableWarning.setVisible(true);
				fadeOutNEW.setNode(nonExecutableWarning);
				fadeOutNEW.setFromValue(0.0);
				fadeOutNEW.setToValue(1.0);
				fadeOutNEW.setCycleCount(2);
				fadeOutNEW.setAutoReverse(true);
				fadeOutNEW.playFromStart();
			}
		}
	}

	public void browseFiles() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Browse Files");
		File file = chooser.showOpenDialog(primaryStage);
		if (file != null) {
			filePath.setText(file.getName());
			fileChosen = file;
			addFileButton.setManaged(true);
			addFileButton.setVisible(true);
			filePath.requestFocus();
		} else {
			nonExecutableWarning.setManaged(true);
			nonExecutableWarning.setVisible(true);
			fadeOutNEW.setNode(nonExecutableWarning);
			fadeOutNEW.setFromValue(0.0);
			fadeOutNEW.setToValue(1.0);
			fadeOutNEW.setCycleCount(2);
			fadeOutNEW.setAutoReverse(true);
			fadeOutNEW.playFromStart();
		}
	}

	public void updateFilePath() {
		int selectedIndex = fileLabelsListView.getSelectionModel().getSelectedIndex();
		App tempApp = new App(fileChosen.getAbsolutePath(), fileChosen.getName());
		filePath.clear();
		fileLabelsListView.getItems().remove(selectedIndex);
		script.getFiles().remove(selectedIndex);
		script.getFiles().add(selectedIndex, tempApp);
		fileLabelsListView.getItems().add(selectedIndex, tempApp.getLabel());
		fileLabelsListView.getSelectionModel().select(selectedIndex);

		fileChosen = null;
		updateFilePathButton.setVisible(false);
		updateFilePathButton.setManaged(false);
	}

	public void clearFiles() {
		fileLabelsListView.getItems().clear();
		script.getFiles().clear();
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

	public void createScript(File file) throws IOException {
		FileOutputStream fos=new FileOutputStream(file);
		DataOutputStream dos=new DataOutputStream(fos);
		String newLine = System.getProperty("line.separator");
		browserChosen = websiteBrowsers.getSelectionModel().getSelectedItem();
		String browserEXE = new String();
		String newWindow = new String();
		if (isWindows()) {
			if (browserChosen == "Google Chrome") {
				browserEXE = "chrome.exe";
				newWindow = "-new-window";
			}
			else if (browserChosen == "FireFox") {
				browserEXE = "firefox.exe";
				newWindow = "-new-instance";
			}
			/*else if (browserChosen == "Internet Explorer") {
				browserEXE = "iexplore.exe";
				newWindow = " ";
			}*/
			if (script.getWebsites().isEmpty() == false) {
				dos.writeBytes(batchWebsiteSection);
				dos.writeBytes(newLine);
				dos.writeBytes("call start " + browserEXE + " " + newWindow + " ");
				for (Website website: script.getWebsites()) {
					if (website.getURL().startsWith("https://") == false && website.getURL().startsWith("http://") == false) {
						website = new Website("https://" + website.getURL(), website.getLabel());
					}
					dos.writeBytes(website.getURL() + " ");
				}
				dos.writeBytes(newLine);
			}
			if (script.getFiles().isEmpty() == false) {
				dos.writeBytes(batchFileSection);
				dos.writeBytes(newLine);
			}
			for (App currApp: script.getFiles()) {
				dos.writeBytes("start \"\" \"" + currApp.getPath() + "\"");
				dos.writeBytes(newLine);
			}
			dos.close();
			fos.close();
		} 
		else if (isMac()) {
			dos.writeBytes("#!/bin/bash");
			dos.writeBytes(newLine);
			/*if (browserChosen == "Safari") {
				browserEXE = "Safari";
			}*/
			if (browserChosen == "Google Chrome") {
				browserEXE = "Google Chrome";
			}

			if (script.getWebsites().isEmpty() == false) {
				dos.writeBytes(bashWebsiteSection);
				dos.writeBytes(newLine);
			}

			for (Website website: script.getWebsites()) {
				if (website.getURL().startsWith("www.") || website.getURL().startsWith("https://") == false && website.getURL().startsWith("http://") == false) {
					website = new Website("https://" + website.getURL(), website.getLabel());
				}
				if (script.getWebsites().get(0) == website) {
					dos.writeBytes("open -na " + browserEXE + " --args --new-window " + website.getURL()); 
					dos.writeBytes(newLine);
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
				else if (script.getWebsites().get(script.getWebsites().size() - 1) == website) {
					dos.writeBytes("open -na " + browserEXE + " " + website.getURL()); 
					dos.writeBytes(newLine);
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
				else {
					dos.writeBytes("open -na " + browserEXE + " " + website.getURL()); 
					dos.writeBytes(newLine);
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
			}
			if (script.getFiles().isEmpty() == false) {
				dos.writeBytes(bashFileSection);
				dos.writeBytes(newLine);
			}

			for (App currApp: script.getFiles()) {
				dos.writeBytes("open '" + currApp.getPath() + "'"); 
				dos.writeBytes(newLine);
			}
			dos.close();
			fos.close();
			file.setExecutable(true);
		}
	}

	public void readScript(File file) throws FileNotFoundException {
		if (file != null) {
			Scanner sc = new Scanner(file);
			if (sc.hasNextLine()) {
				String currLine = sc.nextLine();
				if (isWindows()) {
					if (currLine.equals(batchWebsiteSection)) {
						if (sc.hasNextLine()) {
							currLine = sc.nextLine();
						}
						while (currLine.startsWith("call")) {
							String[] lineSections = currLine.split(" ");
							if (lineSections[2].equals("chrome.exe")) {
								browserChosen = "Google Chrome";
							}
							else if (lineSections[2].equals("firefox.exe")) {
								browserChosen = "FireFox";
							}
							/*else if (lineSections[2].equals("iexplore.exe")) {
								browserChosen = "Internet Explorer";
							}*/
							for (int i = 4; i < lineSections.length; i++) {
								String url = lineSections[i];
								String label = url.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
								addWebsite(new Website(url, label));
							}
							if (sc.hasNextLine()) {
								currLine = sc.nextLine();
							}
							else {
								break;
							}
						}
					}
					if (currLine.equals(batchFileSection)) {
						while (sc.hasNextLine()) {
							currLine = sc.nextLine();
							String[] lineSections = currLine.split(" ");
							lineSections = lineSections[2].split("\"");
							String path = lineSections[1];
							fileChosen = new File(path);
							addFile();
						}
					}
				}
				if (isMac()) {
					if (currLine.equals("#!/bin/bash")) {
						currLine = sc.nextLine();
					}
					if (currLine.equals(bashWebsiteSection)) {
						if (sc.hasNextLine()) {
							currLine = sc.nextLine();
						}
						while (currLine.startsWith("open") || currLine.startsWith("wait")) {
							if (currLine.startsWith("open")) {
								String[] lineSections = currLine.split("open -na ");
								/*if (lineSections[1].startsWith("'Safari'")) {
									browserChosen = "Safari";
								}*/
								if (lineSections[1].startsWith("'Google Chrome'")) {
									browserChosen = "Google Chrome";
								}
								lineSections = lineSections[1].split(" ");
								String url = lineSections[lineSections.length - 1];
								String label = url.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
								addWebsite(new Website(url, label));
							}
							if (sc.hasNextLine()) {
								currLine = sc.nextLine();
							}
							else {
								break;
							}
						}
					}
					if (currLine.equals(bashFileSection)) {
						while (sc.hasNextLine()) {
							currLine = sc.nextLine();
							String[] lineSections = currLine.split("open ");
							lineSections = lineSections[1].split("'");
							String path = lineSections[1];
							fileChosen = new File(path);
							addFile();
						}
					}
				}
			}
			sc.close();
		}
		saveScriptButton.setVisible(false);
		saveScriptButton.setManaged(false);
		updateScriptButton.setVisible(true);
		updateScriptButton.setManaged(true);
	}

	public void updateScript() {
		try {
			PrintWriter writer = new PrintWriter(fileToUpdate);
			writer.print("");
			writer.close();
			createScript(fileToUpdate);
		} catch (IOException e) {

		}
		updateScriptButton.setVisible(false);
		updateScriptButton.setManaged(false);
		saveScriptButton.setVisible(true);
		saveScriptButton.setManaged(true);
	}


	public Script getScript() {
		return script;
	}
}
