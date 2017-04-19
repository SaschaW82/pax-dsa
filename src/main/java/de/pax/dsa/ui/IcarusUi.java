package de.pax.dsa.ui;

import de.pax.dsa.ui.internal.TwoStageMoveNode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IcarusUi extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		TwoStageMoveNode nodeA = new TwoStageMoveNode(150, 150);
		TwoStageMoveNode nodeB = new TwoStageMoveNode(300, 300);

		Button button = new Button("Do Moves");

		button.setOnAction(e -> {
			nodeA.commitMove();
			nodeB.commitMove();
		});

		final Group group = new Group(nodeA, nodeB, button);

		// layout the scene.
		final StackPane background = new StackPane();
		background.setStyle("-fx-background-color: cornsilk;");
		final Scene scene = new Scene(new Group(background, group), 600, 500);
		background.prefHeightProperty().bind(scene.heightProperty());
		background.prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();

	}

}