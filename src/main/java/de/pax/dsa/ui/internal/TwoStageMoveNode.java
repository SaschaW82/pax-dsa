package de.pax.dsa.ui.internal;

import java.util.function.Consumer;

import de.pax.dsa.model.PositionUpdate;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class TwoStageMoveNode extends Group {

	private Anchor position;
	private Anchor moveTarget;
	private Consumer<PositionUpdate> targetChangedConsumer = t -> {
	};

	public TwoStageMoveNode(String string, double x, double y) {
		Line line = new Line(x, y, x, y);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStroke(Color.CORNFLOWERBLUE);
		line.setStrokeWidth(5);
		line.setMouseTransparent(true);

		position = new Anchor("position", line.startXProperty(), line.startYProperty());
		moveTarget = new Anchor("marker", line.endXProperty(), line.endYProperty());
		moveTarget.setFill(Color.TRANSPARENT);

		moveTarget.getStrokeDashArray().addAll(2d, 20d);

		position.setMouseTransparent(true);

		DragEnabler.enableDrag(moveTarget, targetChangedConsumer);

		getChildren().addAll(line, position, moveTarget);

		setId(string);
	}

	public void setMoveTarget(double x, double y) {
		MoveCenterTransition move = new MoveCenterTransition(moveTarget, x, y);
		move.play();
	}

	public void commitMove() {
		MoveCenterTransition move = new MoveCenterTransition(position, moveTarget.getCenterX(),
				moveTarget.getCenterY());
		move.play();
	}

	public void onTargetChanged(Consumer<PositionUpdate> targetChangedConsumer) {
		this.targetChangedConsumer = targetChangedConsumer;
	}

}
