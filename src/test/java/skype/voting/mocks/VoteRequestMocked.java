package skype.voting.mocks;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.requests.VoteRequest;

public class VoteRequestMocked extends VoteRequest {

	public VoteRequestMocked(ChatAdapterInterface chat, int vote) {
		super(chat, null,vote);
	}
		
	public VoteRequestMocked(String user, int vote) {
		super(new ChatBridgeMock("autoid", user), "#"+vote, vote);
	}
}
