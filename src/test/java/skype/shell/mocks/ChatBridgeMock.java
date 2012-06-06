package skype.shell.mocks;

import java.util.LinkedList;
import java.util.List;

import skype.ChatAdapterInterface;

public class ChatBridgeMock implements ChatAdapterInterface {

	private final String id;
	private final String sender;

	public ChatBridgeMock(String id, String sender) {
		this.id = id;
		this.sender = sender;
	}
	
	public ChatBridgeMock(String id){
		this(id, "none");
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public String getSenderFullName() {
		return sender;
	}

	@Override
	public List<String> getPartipantNames() {
		return participants;
	}

	public void addParticipant(String participantName) {
		participants.add(participantName);
	}
	List<String> participants = new LinkedList<String>();
}
