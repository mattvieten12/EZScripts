
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
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is the main file for the EZScripts Application.
 * Copyright: Do not distribute this application without owner's consent, "Matt Vieten"
 * Email: matt.vieten12@gmail.com
 * @author mvieten
 *
 */
public class ApplicationWindow extends Application {


	private static TextField websiteURL;
	private static TextField filePath;

	private static File fileChosen;
	private static File fileToUpdate;

	private static ComboBox<String> websiteBrowsers;
	private static String browserChosen;

	private static TabPane tabPane;
	private static Tab createTab;
	private static Tab updateTab;

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
	final static Button updateWebsiteURLButton = new Button("Update Website!");
	final static Button clearWebsiteButton = new Button("Clear All Websites");
	final static Button createScriptButton = new Button("Create Shortcut!");

	final static Button browseFilesButton = new Button("Browse files...");
	final static Button addFileButton = new Button("Add File!");
	final static Button removeFileButton = new Button("Remove File");
	final static Button updateFileLVButton = new Button("Update File");
	final static Button clearFileButton = new Button("Clear All Files");
	final static Button updateFilePathButton = new Button("Update!");
	final static Button popupCreateButton = new Button("CREATE!");

	final static Button updateScriptButton = new Button(("Update Script!"));

	private final static Label websiteLabelWarning = new Label("Invalid URL, please check that the url is correct.");
	private final static Label nonExecutableWarning = new Label("Please select a file or application!");
	private final static Label fileAlreadyExistsWarning = new Label("File already exists, please select a different name.");
	private final static String regex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
	private final static String OS = System.getProperty("os.name").toLowerCase();
	private final static String batchWebsiteSection = new String("::Website Section");
	private final static String batchFileSection = new String("::File Section");
	private final static String bashWebsiteSection = new String("#Website Section");
	private final static String bashFileSection = new String("#File Section");


	/**
	 * Main method that launches the gui application.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	//starting point for the application
	//this is where we put the code for the user interface

	/**
	 * This method creates the GUI application and initializes all of its' properties.
	 * @param stage - Main stage for application
	 */
	@Override
	public void start(Stage stage) throws IOException {	

		/**
		 * Initializes the list of websites and the list of files kept by the application.
		 */
		scriptSites = new ArrayList<Website>();
		scriptFiles = new ArrayList<App>();

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
				addWebsiteButton.fire();
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
				addFileButton.fire();
			}
		});

		/**
		 * Initializes the website browser combo box and adds different browsers (some dependent on OS).
		 */
		websiteBrowsers = new ComboBox<String>();
		ArrayList<String> browserOptions = new ArrayList<String>();
		if (isMac()) {
			browserOptions.add("Safari");
		}
		browserOptions.add("Google Chrome");
		//browserOptions.add("Internet Explorer");
		browserOptions.add("FireFox");
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
		 * Sets title of application to "EZScripts" and adds main panes to the application.
		 */
		stage.setTitle("EZScripts");
		BorderPane appLayout = new BorderPane();
		BorderPane componentLayout = new BorderPane();
		componentLayout.setPadding(new Insets(20,0,20,20));

		/**
		 * Creates tab pane and tabs at the top of application for creating new scripts and updating old scripts.
		 */
		tabPane = new TabPane();
		createTab = new Tab();
		createTab.setText("Create New Script");
		tabPane.getTabs().add(createTab);
		updateTab = new Tab();
		updateTab.setText("Update Old Script");
		tabPane.getTabs().add(updateTab);
		appLayout.setTop(tabPane);
		setUpdateTabEvent();

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
						websiteLabelWarning.setManaged(true);
						websiteLabelWarning.setVisible(true);
					}
				}
				else {
					websiteLabelWarning.setManaged(true);
					websiteLabelWarning.setVisible(true);
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
			}
		});

		/**
		 * When the update script button is pressed, it fires the update script method and fixes the tab.
		 */
		updateScriptButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				createTab.setDisable(false);
				updateTab.setOnSelectionChanged(null);
				tabPane.getSelectionModel().select(createTab);
				setUpdateTabEvent();
				updateScript();
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
		websiteLabelWarning.setManaged(false);
		websiteLabelWarning.setVisible(false);
		websiteURLPane.getChildren().add(websiteLabelWarning);

		/**
		 * Adds the non executable warning to the file path pane.
		 */
		nonExecutableWarning.setTextFill(Color.RED);
		nonExecutableWarning.setManaged(false);
		nonExecutableWarning.setVisible(false);
		filePathPane.getChildren().add(nonExecutableWarning);

		/**
		 * Sets various components invisible (until conditions are met).
		 */
		fileAlreadyExistsWarning.setTextFill(Color.RED);
		fileAlreadyExistsWarning.setVisible(false);
		fileAlreadyExistsWarning.setManaged(false);

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
		bottomPane.getChildren().add(createScriptButton);
		bottomPane.getChildren().add(updateScriptButton);

		/**
		 * Makes update script button invisible (until conditions are met) and adds the bottom pane to the main application pane.
		 */
		updateScriptButton.setVisible(false);
		updateScriptButton.setManaged(false);
		componentLayout.setBottom(bottomPane);

		/**
		 * When the create script button is pressed, the process of creating the script file is triggered, and it brings up the popup to let the user name the script and confirm its' creation.
		 */
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
						dialogScene.getStylesheets().add(getClass().getResource("windowsStyles.css").toExternalForm());
					}

					/**
					 * When the popup create button is pressed, it fires the create script method with the given file name.
					 */
					popupCreateButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (scriptName != null) {

								try {
									File file = null;
									if (isWindows()) {
										file=new File(System.getProperty("user.home") + "\\Desktop\\" + scriptName.getText() + ".bat");
									}
									else if (isMac()) {
										file = new File(System.getProperty("user.home") + "/Desktop/" + scriptName.getText() + ".command"); 
									}
									if (file.isFile()) {
										fileAlreadyExistsWarning.setVisible(true);
										fileAlreadyExistsWarning.setManaged(true);
									}
									else {
										createScript(file);
									}
									clearWebsites();
									clearFiles();
									dialog.hide();

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					});



					/**
					 * Adds popup components to different panes and then adds those panes to the popup window pane.
					 */
					centerPane.getChildren().add(popupCreateButton);
					bottomPane.getChildren().add(fileAlreadyExistsWarning);
					fileAlreadyExistsWarning.setVisible(false);
					fileAlreadyExistsWarning.setManaged(false);
					popupPane.setCenter(centerPane);
					popupPane.setBottom(bottomPane);
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


		appLayout.prefHeightProperty().bind(appScene.heightProperty());
		appLayout.prefWidthProperty().bind(appScene.widthProperty());

		stage.setScene(appScene);
		stage.show();
		stage.setResizable(false);
		primaryStage = stage;

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

					Website tempDragged = scriptSites.get(draggedIdx);
					Website tempDropped = scriptSites.get(thisIdx);

					scriptSites.set(draggedIdx, tempDropped);
					scriptSites.set(thisIdx, tempDragged);

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

					App tempDragged = scriptFiles.get(draggedIdx);
					App tempDropped = scriptFiles.get(thisIdx);

					scriptFiles.set(draggedIdx, tempDropped);
					scriptFiles.set(thisIdx, tempDragged);

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


	public static void setUpdateTabEvent() {
		updateTab.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event t) {
				FileChooser chooser = new FileChooser();
				if (isWindows()) {
					ExtensionFilter filter = new ExtensionFilter("Script Files", "*.bat");
					chooser.getExtensionFilters().add(filter);
				}
				else if (isMac()) {
					ExtensionFilter filter = new ExtensionFilter("Script Files", "*.command");
					chooser.getExtensionFilters().add(filter);
				}
				File file = chooser.showOpenDialog(primaryStage);
				if (file != null) {
					fileToUpdate = file;
					try {
						System.out.println(1);
						clearWebsites();
						clearFiles();
						readScript(file);
						websiteBrowsers.getSelectionModel().select(browserChosen);
						createTab.setDisable(true);

					} catch (FileNotFoundException e) {

					}
				}
				else {
					tabPane.getSelectionModel().select(createTab);
				}
			}
		});
	}

	public static void addWebsite(Website website) {
		websiteLabelWarning.setManaged(false);
		websiteLabelWarning.setVisible(false);
		removeWebsiteButton.setVisible(true);
		updateWebsiteLVButton.setVisible(true);
		clearWebsiteButton.setVisible(true);
		scriptSites.add(website);
		websiteURL.clear();
		websiteLabelsListView.getItems().addAll(website.getLabel());
		websiteLabelsListView.getSelectionModel().selectLast();
		websiteURL.requestFocus();
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
					removeWebsiteButton.setDisable(false);
					clearWebsiteButton.setDisable(false);
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
		websiteLabelWarning.setVisible(false);
		websiteLabelWarning.setManaged(false);
	}

	public static void addFile() {
		scriptFiles.add(new App(fileChosen.getAbsolutePath(), fileChosen.getName()));
		fileLabelsListView.getItems().add(fileChosen.getName());
		fileLabelsListView.getSelectionModel().selectLast();
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

			FileChooser chooser = new FileChooser();
			File file = chooser.showOpenDialog(primaryStage);
			if (file != null) {
				updateFilePathButton.setVisible(true);
				updateFilePathButton.setManaged(true);
				nonExecutableWarning.setManaged(false);
				nonExecutableWarning.setVisible(false);
				filePath.setText(file.getName());
				fileChosen = file;
			} else {
				nonExecutableWarning.setManaged(true);
				nonExecutableWarning.setVisible(true);
			}
		}
	}

	public static void browseFiles() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(primaryStage);
		if (file != null) {
			nonExecutableWarning.setManaged(false);
			nonExecutableWarning.setVisible(false);
			filePath.setText(file.getName());
			fileChosen = file;
			addFileButton.setManaged(true);
			addFileButton.setVisible(true);
			filePath.requestFocus();
		} else {
			nonExecutableWarning.setManaged(true);
			nonExecutableWarning.setVisible(true);
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
		nonExecutableWarning.setVisible(false);
		nonExecutableWarning.setManaged(false);
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static void createScript(File file) throws IOException {
		FileOutputStream fos=new FileOutputStream(file);
		DataOutputStream dos=new DataOutputStream(fos);
		String newLine = System.getProperty("line.separator");
		browserChosen = websiteBrowsers.getSelectionModel().getSelectedItem();
		String browserEXE = new String();
		if (isWindows()) {
			if (browserChosen == "Google Chrome") {
				browserEXE = "chrome.exe";
			}
			else if (browserChosen == "FireFox") {
				browserEXE = "firefox.exe";
			}
			/*else if (browserChosen == "Internet Explorer") {

				}*/
			if (scriptSites.isEmpty() == false) {
				dos.writeBytes(batchWebsiteSection);
				dos.writeBytes(newLine);
			}
			for (Website website: scriptSites) {
				if (website.getURL().startsWith("www.") || website.getURL().startsWith("https://") == false && website.getURL().startsWith("http://") == false) {
					website = new Website("https://" + website.getURL(), website.getLabel());
				}
				if (scriptSites.get(0) == website) {
					dos.writeBytes("call start /w " + browserEXE + " -new-window " + website.getURL());
					dos.writeBytes(newLine);
				}
				else {
					dos.writeBytes("call start /w " + browserEXE + " " + website.getURL()); 
					dos.writeBytes(newLine);
				}
			}

			if (scriptFiles.isEmpty() == false) {
				dos.writeBytes(batchFileSection);
				dos.writeBytes(newLine);
			}
			for (App currApp: scriptFiles) {
				dos.writeBytes("\"" + currApp.getPath() + "\"");
				dos.writeBytes(newLine);
			}
			dos.close();
			fos.close();
		} 
		else if (isMac()) {
			dos.writeBytes("#!/bin/bash");
			dos.writeBytes(newLine);

			if (browserChosen == "Safari") {
				browserEXE = "Safari";
			}
			else if (browserChosen == "Google Chrome") {
				browserEXE = "Google Chrome";
			}
			else if (browserChosen == "FireFox") {
				browserEXE = "FireFox";
			}
			if (scriptSites.isEmpty() == false) {
				dos.writeBytes(bashWebsiteSection);
				dos.writeBytes(newLine);
			}

			for (Website website: scriptSites) {
				if (website.getURL().startsWith("www.") || website.getURL().startsWith("https://") == false && website.getURL().startsWith("http://") == false) {
					website = new Website("https://" + website.getURL(), website.getLabel());
				}
				if (scriptSites.get(0) == website) {
					dos.writeBytes("open -na 'Google Chrome' --args --new-window " + website.getURL()); 
					dos.writeBytes(newLine);
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
				else if (scriptSites.get(scriptSites.size() - 1) == website) {
					dos.writeBytes("open -na 'Google Chrome' " + website.getURL()); 
					dos.writeBytes(newLine);
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
				else {
					dos.writeBytes("open -na 'Google Chrome' " + website.getURL()); 
					dos.writeBytes(newLine);
					dos.writeBytes("wait");
					dos.writeBytes(newLine);
				}
			}
			if (scriptFiles.isEmpty() == false) {
				dos.writeBytes(bashFileSection);
				dos.writeBytes(newLine);
			}

			for (App currApp: scriptFiles) {
				dos.writeBytes("open '" + currApp.getPath() + "'"); 
				dos.writeBytes(newLine);
			}
			dos.close();
			fos.close();
			file.setExecutable(true);
		}
	}

	public static void readScript(File file) throws FileNotFoundException {
		if (file != null) {
			Scanner sc = new Scanner(file);
			System.out.println(2);
			if (sc.hasNextLine()) {
				String currLine = sc.nextLine();
				if (isWindows()) {
					if (currLine.equals(batchWebsiteSection)) {
						if (sc.hasNextLine()) {
							currLine = sc.nextLine();
						}
						while (currLine.startsWith("call")) {
							String[] lineSections = currLine.split(" ");
							if (lineSections[3].equals("chrome.exe")) {
								browserChosen = "Google Chrome";
							}
							else if (lineSections[3].equals("firefox.exe")) {
								browserChosen = "FireFox";
							}
							/*else if (lineSections[3].equals("Internet Explorer")) {
							browserChosen = "Internet Explorer";
							}*/
							String url = lineSections[lineSections.length - 1];
							String label = url.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
							addWebsite(new Website(url, label));
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
							String[] lineSections = currLine.split("\"");
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
								System.out.println(lineSections[1]);
								if (lineSections[1].startsWith("'Safari'")) {
									browserChosen = "Safari";
								}
								else if (lineSections[1].startsWith("'Google Chrome'")) {
									browserChosen = "Google Chrome";
								}
								else if (lineSections[1].startsWith("'FireFox'")) {
									browserChosen = "FireFox";
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
		createScriptButton.setVisible(false);
		createScriptButton.setManaged(false);
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
		clearFiles();
		clearWebsites();
		updateScriptButton.setVisible(false);
		updateScriptButton.setManaged(false);
		createScriptButton.setVisible(true);
		createScriptButton.setManaged(true);
	}
}