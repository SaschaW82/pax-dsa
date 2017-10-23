package de.pax.dsa.chatpane;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.messages.TextMessage;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class ChatPaneController {

	@FXML
	private ListView<String> listView;

	@FXML
	private TextArea chatArea;

	@FXML
	private TextArea sendArea;

	@Inject
	private IIcarusSession session;

	@PostConstruct
	private void postConstruct() {
		ObservableList<String> items = listView.getItems();
		items.setAll("not", "connected", "to", "session");

		listView.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
					// user selected
					// System.out.println(newValue);
				});

		sendArea.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				event.consume();
				session.sendMessage(new TextMessage(sendArea.getText()));
				chatArea.appendText(formatMessage(session.getUserName(), sendArea.getText()));
				sendArea.clear();
			}
		});

		session.onSessionConnected(this::sessionConnected);
	}

	private void sessionConnected(Boolean connected) {
		if (connected) {
			ObservableList<String> items = listView.getItems();

			items.setAll(session.getAllOtherUsers());

			session.onUserEntered(userName -> {
				items.setAll(session.getAllOtherUsers());
			});

			session.onMessageReceived(TextMessage.class, message -> {
				chatArea.appendText(formatMessage(message.getSender(), message.getText()));
			});
		}
	}

	private String formatMessage(String sender, String text) {
		return "[" + sender + "]: " + text + "\n";
	}

}
