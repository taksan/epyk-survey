package skype.shell.mocks;

import java.util.LinkedList;
import java.util.List;

import com.skype.ChatListener;
import com.skype.User;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.skype.mocks.SkypeBridgeMock;

public class ChatBridgeMock implements ChatAdapterInterface 
{
	private final class DoNothingListener implements ChatListener {
		@Override
		public void userLeft(User user) {
		}

		@Override
		public void userAdded(User user) {
		}
	}

	List<String> participants = new LinkedList<String>();
	ChatListener listener = new DoNothingListener();
	private final String id;
	String sender;
	SkypeBridge skypeBriddgeMock = new  SkypeBridgeMock();

	public ChatBridgeMock(String id, String sender) {
		this.id = id;
		setLastSender(sender);
	}
	
	public ChatBridgeMock(String id){
		this(id, "none");
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public String getLasterSenderFullName() {
		return sender;
	}

	@Override
	public List<String> getPartipantNames() {
		return participants;
	}

	public void addParticipant(String participantName) {
		participants.add(participantName);
		listener.userAdded(asUser(participantName));
		
	}

	private User asUser(String participantName) {
		return User.getInstance(participantName);
	}
	
	public void removeParticipant(String participantName) {
		participants.remove(participantName);
		listener.userLeft(asUser(participantName));
	}
	
	@Override
	public void addListener(ChatListener listener) {
		this.listener = listener;
	}

	@Override
	public SkypeBridge getSkypeBridge() {
		return skypeBriddgeMock;
	}

	@Override
	public void removeListener(ChatListener weakReference) {
		listener = new DoNothingListener();
	}

	@Override
	public void setLastSender(String senderFullNameOrId) {
		this.sender = senderFullNameOrId;
	}
}
