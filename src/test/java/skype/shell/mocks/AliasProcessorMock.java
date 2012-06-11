package skype.shell.mocks;

import skype.ChatAdapterInterface;
import skype.shell.AliasProcessor;
import skype.shell.ReplyTextRequest;
import skype.shell.ShellCommand;

public class AliasProcessorMock implements AliasProcessor {

	StringBuilder operations = new StringBuilder();  
			
	private String understandsMessage="";

	private String alias="";
	private String expanded="";

	@Override
	public String expandMessage(String message) {
		operations.append("expandMessage:"+message+"\n");
		if (message.equals(alias)) {
			operations.append("message expanded to:"+expanded+"\n");
			return expanded;
		}
		return message;
	}

	@Override
	public boolean understands(String message) {
		operations.append("understands:"+message+"\n");
		return understandsMessage.equals(message);
	}

	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		operations.append("processMessage:"+message+"\n");
		return new ReplyTextRequest(chat, message, "");
	}

	public void setUnderstands(String message) {
		understandsMessage = message;
	}

	public String getOperations() {
		return operations.toString();
	}

	public void setAlias(String alias, String expanded) {
		this.alias = alias;
		this.expanded = expanded;
	}

}
