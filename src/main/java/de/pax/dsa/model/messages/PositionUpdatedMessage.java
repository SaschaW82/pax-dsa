package de.pax.dsa.model.messages;

import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.StringMapper;

public class PositionUpdatedMessage implements IMessage {

	private String id;
	private double x;
	private double y;
	private String sender;

	public PositionUpdatedMessage(String id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public PositionUpdatedMessage(Message message, String sender) {
		this.sender = sender;
		Map<String, String> map = StringMapper.keyValueListStringToMap(message.getBody());
		this.id = map.get("id");
		this.x = Double.parseDouble(map.get("x"));
		this.y = Double.parseDouble(map.get("y"));
	}

	public String getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	@Override
	public String getSender() {
		return sender;
	}

	@Override
	public String toString() {
		return "PositionUpdate [id=" + id + ", x=" + x + ", y=" + y + "]";
	}

	public static String startsWith() {
		return "PositionUpdate [";
	}
}
