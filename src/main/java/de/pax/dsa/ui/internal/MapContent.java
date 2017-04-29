package de.pax.dsa.ui.internal;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.PositionUpdate;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import de.pax.dsa.ui.internal.nodes.GridFactory;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import de.pax.dsa.ui.internal.nodes.TwoStageMoveNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;

public class MapContent {

	@Inject
	private IIcarusSession session;

	@Inject
	private Logger logger;

	private TwoStageMoveNode nodeA;

	private TwoStageMoveNode nodeB;

	public Group build() {

		nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		nodeA.setMoveTarget(100, 300);
		nodeB = new TwoStageMoveNode("nodeB", 200, 100);
		nodeB.setMoveTarget(200, 500);
		ImageNode img = new ImageNode("file:src/main/resources/festum.png", 300, 200);

		Consumer<PositionUpdate> onDragComplete = session::sendPositionUpdate;
		DragEnabler.enableDrag(nodeA, onDragComplete);
		DragEnabler.enableDrag(nodeB, onDragComplete);
		DragEnabler.enableDrag(img, onDragComplete);

		Group group = new Group(img, nodeA, nodeB);

		session.onPositionUpdate(positionUpdate -> {
			I2DObject node = getFromGroup(positionUpdate.getId(), group);
			logger.info("received " + positionUpdate);
			node.setX(positionUpdate.getX());
			node.setY(positionUpdate.getY());
		});

		Canvas grid = GridFactory.createGrid(50);
		grid.setStyle("-fx-background-color: cornsilk;");
		Group root = new Group(group, grid);
		return root;
	}

	public void doMoves() {
		nodeA.commitMove();
		nodeB.commitMove();
	}

	private I2DObject getFromGroup(String id, Group group) {
		List<Node> collect = group.getChildren().stream().filter(e -> id.equals(e.getId()))
				.collect(Collectors.toList());
		if (collect.size() != 1) {
			throw new IllegalStateException("ID: " + id + "exists not exactly once (" + collect.size() + " times)");
		} else {
			return (I2DObject) collect.get(0);
		}
	}

}