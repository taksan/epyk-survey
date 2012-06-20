package skype.shell;

import skype.ChatAdapterInterface;
import skype.alias.HelpProvider;
import skype.voting.CommandExecutor;
import skype.voting.HelpVisitor;

public class HelpCommandExecutor implements CommandExecutor {

	private ReplyListener listener;
	private HelpProvider[] helpers;
	
	public HelpCommandExecutor(HelpProvider... helperProviders) {
		helpers = helperProviders;
	}

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		if (!isHelpRequest(message))
			return false;
		final StringBuilder helpMessage = new StringBuilder();
		helpMessage.append("HELP.\n");
		for (HelpProvider h : helpers) {
			h.acceptHelpVisitor(new HelpVisitor() {
				@Override
				public HelpVisitor onTopLevel(String topicMessage) {
					helpMessage.append("* "+topicMessage+"\n");
					return this;
				}
				
				@Override
				public HelpVisitor onTopic(String subtopic) {
					helpMessage.append("    "+subtopic+"\n");
					return this;
				}
				
				@Override
				public HelpVisitor onTopicDescription(String topicDescription) {
					helpMessage.append("        "+topicDescription+"\n");
					return this;
				}
			});
		}
		
		listener.onReplyPrivate(chat, helpMessage.toString());
		
		return true;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}

	private boolean isHelpRequest(String message) {
		return message.trim().toLowerCase().equals("#help");
	}
}
