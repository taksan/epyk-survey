package skype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.UnhandledException;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class ChatBridge implements ChatAdapterInterface  {

	public final Chat chat;
	private final String sender;

	public ChatBridge(Chat chat, String sender) {
		this.chat = chat;
		this.sender = sender;
	}

	public Chat getChat() {
		return chat;
	}

	@Override
	public String getSenderFullName() {
		return sender;
	}

	@Override
	public List<String> getPartipantNames() {
		List<String> participants = new ArrayList<String>();
		try {
			User[] allMembers = chat.getAllMembers();
			for (User user : allMembers) {
				participants.add(SkypeBridgeImpl.getUserFullNameOrId(user));
			}
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
		return participants;
	}

}
