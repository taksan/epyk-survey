package skype.lunch;

import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;

public class LunchProcessor implements CommandProcessor {

	private ReplyListener listener;

	@Override
	public void processLunchRequest(LunchRequest lunchRequest) {
		String reply = buildVotingMenu(lunchRequest);
		listener.onReply(lunchRequest.getChat(), reply);
	}

	String [] lunchOptions=new String[0];
	private String buildVotingMenu(LunchRequest lunchRequest) {
		lunchOptions = new String[lunchRequest.getOptionCount()];
		final StringBuffer msg = new StringBuffer();
		msg.append("Almoço!\n");
		lunchRequest.accept(new LunchRequestVisitor() {
			int count=1;
			@Override
			public void visit(LunchOption option) {
				lunchOptions[count-1]= option.getName();
				msg.append(count+") ");
				msg.append(option.getName());
				msg.append("\n");
				count++;
			}
		});
		String reply = "\n"+msg.toString().trim();
		return reply;
	}

	@Override
	public void processUnrecognizedCommand(UnrecognizedCommand unrecognizedCommand) {
		if (!unrecognizedCommand.getText().startsWith("#"))
			return;
		
		listener.onReply(
				unrecognizedCommand.getChat(), 
				String.format("'%s' not recognized", unrecognizedCommand.getText()));
	}
	
	@Override
	public void addReplyListener(ReplyListener listener) {
		this.listener = listener;
	}

	@Override
	public void processVoteRequest(VoteRequest request) {
		if (lunchOptions.length == 0)
			return;
		
		String optionName = lunchOptions[request.vote-1];
		String message = request.sender + " votou em " + optionName;
		listener.onReply(request.getChat(), message);
	}
}
