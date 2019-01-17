
//Imports are listed in full to show what's being used
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * This class is the main file for the EZScripts Application.
 * Copyright: Do not distribute this application without owner's consent, "Matt Vieten"
 * Email: matt.vieten12@gmail.com
 * @author mvieten
 *
 */
public class ApplicationWindow extends Application {

	private ArrayList<ApplicationPage> appPages;
	protected static TabPane tabPane;
	protected static Stage primaryStage;

	protected static ArrayList<String> scriptFileNames = new ArrayList<String>();
	
	protected static int untitledIndex;

	private Scene appScene;

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
		 * Sets title of application to "EZScripts"
		 */
		stage.setTitle("EZScripts");
		primaryStage = stage;
		/**
		 * Creates tab pane and tabs at the top of application for creating new scripts and updating old scripts.
		 */
		tabPane = new TabPane();
		
		untitledIndex = 1;
		Tab newTab = new Tab("+");
		newTab.setClosable(false);
		tabPane.getTabs().add(newTab);
		Tab curr = createAndSelectNewTab(tabPane, "Untitled");
		curr.setOnClosed(event -> {
			scriptFileNames.remove(curr.getText());
			int appPageIndex = tabPane.getSelectionModel().getSelectedIndex();
			appPages.remove(appPageIndex);
		});
		appPages = new ArrayList<ApplicationPage>();
		ApplicationPage currAppPage = new ApplicationPage(new Script(new ArrayList<Website>(), new ArrayList<App>()));
		appPages.add(currAppPage);
		System.out.println("App page: " + appPages.get(0).appScene.getHeight());
		//appPages.get(appPages.size() - 1).appLayout.setTop(tabPane);

		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable,
					Tab oldSelectedTab, Tab newSelectedTab) {
				int index = tabPane.getTabs().indexOf(newSelectedTab);
				if (newSelectedTab == newTab) {
					ApplicationPage newAppPage = new ApplicationPage(new Script(new ArrayList<Website>(), new ArrayList<App>()));
					appPages.add(newAppPage);
					index = tabPane.getTabs().indexOf(newSelectedTab);
					Tab curr = createAndSelectNewTab(tabPane, "Untitled " + (untitledIndex));
					untitledIndex++;
					curr.setOnClosed(event -> {
						scriptFileNames.remove(curr.getText());
						int appPageIndex = tabPane.getSelectionModel().getSelectedIndex();
						appPages.remove(appPageIndex);
					});
				}
				appScene = appPages.get(index).appScene;
				//System.out.println(primaryStage.getHeight());
				//System.out.println(primaryStage.getHeight());
				appPages.get(index).appLayout.setTop(tabPane);
				stage.setScene(appScene);
				stage.show();
				System.out.println(stage.getHeight());
				System.out.println("App page: " + appPages.get(index).appScene.getHeight());
				appPages.get(index).websiteURL.requestFocus();
			}
		});
		
		System.out.println(scriptFileNames);

		currAppPage.appLayout.setTop(tabPane);
		appScene = appPages.get(appPages.size() - 1).appScene;

		
		stage.setScene(appScene);
		
		stage.show();
		stage.setResizable(false);
		
		System.out.println(stage.getHeight());
	}

	/*private Tab createAndSelectNewTab(final TabPane tabPane, final String title) {
		Tab tab = new Tab(title);
		final ObservableList<Tab> tabs = tabPane.getTabs();
		tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
		tab.setOnCloseRequest(e -> {
			int index = tabPane.getTabs().indexOf(tab);
			appPages.remove(index);
		});
		tabs.add(tabs.size() - 1, tab);
		tabPane.getSelectionModel().select(tab);
		return tab;
	}*/
	
	private Tab createAndSelectNewTab(TabPane tabPane, String title) {
		Tab tab = new Tab(title);
		ObservableList<Tab> tabs = tabPane.getTabs();
		tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
		tabs.add(tabs.size() - 1, tab);
		tabPane.getSelectionModel().select(tab);
		return tab;
	}
}