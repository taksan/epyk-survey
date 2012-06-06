package skype.lunch;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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

	Map<String, Integer> votesByOption = new LinkedHashMap<String, Integer>();
	@Override
	public void processVoteRequest(VoteRequest request) {
		if (lunchOptions.length == 0)
			return;
		
		String optionName = lunchOptions[request.vote-1];
		if (votesByOption.get(optionName) == null)
			votesByOption.put(optionName, 0);
		votesByOption.put(optionName, votesByOption.get(optionName)+1);
		StringBuilder sb = new StringBuilder("Votes: ");
		for (String option : lunchOptions) {
			if (votesByOption.get(option) == null)
				votesByOption.put(option, 0);
			
			sb.append(option+": " + votesByOption.get(option));
			sb.append(" ; ");
		}
		String message = StringUtils.substring(sb.toString(), 0,-3);
		listener.onReply(request.getChat(), message);
	}
}
