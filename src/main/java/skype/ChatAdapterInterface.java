package skype;

import java.util.List;

import com.skype.ChatListener;

public interface ChatAdapterInterface {

	String getLasterSenderFullName();
	List<String> getPartipantNames();
	void addListener(ChatListener listener);
	SkypeBridge getSkypeBridge();
	void removeListener(ChatListener weakReference);
	void setLastSender(String senderFullNameOrId);
	void setGuidelines(String guidelines);
	void setTopic(String topic);
}