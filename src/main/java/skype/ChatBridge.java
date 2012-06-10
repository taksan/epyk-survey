package skype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.UnhandledException;

import com.skype.Chat;
import com.skype.ChatListener;
import com.skype.SkypeException;
import com.skype.User;

public class ChatBridge implements ChatAdapterInterface  {

	public final Chat chat;
	String lastSender;
	private final SkypeBridge skypeBridge;

	public ChatBridge(Chat chat) {
		this.chat = chat;
		this.skypeBridge = SkypeBridgeImpl.get();
	}

	public Chat getChat() {
		return chat;
	}

	@Override
	public String getLasterSenderFullName() {
		return lastSender;
	}

	@Override
	public List<String> getPartipantNames() {
		List<String> participants = new ArrayList<String>();
		try {
			User[] allMembers = chat.getAllMembers();
			for (User user : allMembers) {
				participants.add(skypeBridge.getUserFullNameOrId(user));
			}
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
		return participants;
	}

	@Override
	public void addListener(ChatListener listener) {
		try {
			chat.addListener(listener);
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
	}
	

	@Override
	public void setGuidelines(String guidelines) {
		try {
			chat.setGuidelines(guidelines);
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
	}
	
	@Override
	public void setTopic(String topic) {
		try {
			chat.setTopic(topic);
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
	}


	@Override
	public SkypeBridge getSkypeBridge() {
		return skypeBridge;
	}

	@Override
	public void removeListener(ChatListener listener) {
		chat.removeListener(listener);
	}

	@Override
	public void setLastSender(String senderFullNameOrId) {
		lastSender = senderFullNameOrId;
	}



}
