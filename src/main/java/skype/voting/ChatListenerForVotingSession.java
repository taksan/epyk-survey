package skype.voting;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.ReplyListener;
import skype.voting.application.VotingSession;

import com.skype.ChatListener;
import com.skype.User;

public class ChatListenerForVotingSession implements ChatListener {

	private final VotingSession targetSession;
	private final ChatAdapterInterface chat;
	private SkypeBridge bridge;
	private ReplyListener listener;
	private final VotingSessionMessages messages;
	
	public ChatListenerForVotingSession(ChatAdapterInterface chat, VotingSession delegate, ReplyListener listener,
			VotingSessionMessages messages) {
		this.chat = chat;
		this.targetSession = delegate;
		this.listener = listener;
		this.messages = messages;
		this.bridge = chat.getSkypeBridge();
		chat.addListener(this);
	}

	@Override
	public void userAdded(User user) {
		String participant = bridge.getUserFullNameOrId(user);
		targetSession.addNewParticipant(participant);
		String status = messages.getVotingStatusMessage(targetSession);
		listener.onReply(chat, 
				String.format(
						"User '%s' added to the voting poll.\n" +
						"Votes: "+status, participant)
				);
		String menu = messages.buildVotingMenuWithoutVoters(targetSession);
		listener.onReplyPrivate(chat,
				String.format(
					"Hey, we are having a voting poll. Come and join us. Here are the options:\n" +
					"%s\n" +
					"Vote by using #1,#2, and so on",
					menu.trim()
					)
				);
	}

	@Override
	public void userLeft(User user) {
		String participant = bridge.getUserFullNameOrId(user);
		targetSession.removeParticipant(participant);
		
		String status = messages.getVotingStatusMessage(targetSession);
		listener.onReply(chat, String.format("User '%s' left the voting poll.\n" +
				"Update Votes: "+status, 
				participant));
	}
}