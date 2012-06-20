package skype.voting.processors;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.ReplyListenerMock;
import skype.voting.VotingPollCommandExecutor;
import skype.voting.application.VotingPollOption;
import skype.voting.mocks.VoteRequestMocked;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.mocks.VotingSessionMessagesMock;
import skype.voting.processors.requests.StartPollRequest;
import skype.voting.processors.requests.VoteRequest;

public class ProcessorTestUtils {

	private final VotingPollCommandExecutor VOTING_POLL_COMMAND_PROCESSOR = new VotingPollCommandExecutor();
	private ChatAdapterInterface chatBridgeMock;

	public ReplyListenerMock initializeProcessorAndGetListener(VotingCommandProcessorAbstract subject) {
		subject.setVoteSessionProvider(VOTING_POLL_COMMAND_PROCESSOR);
		ReplyListenerMock listener = new ReplyListenerMock(); 
		VOTING_POLL_COMMAND_PROCESSOR.setReplyListener(listener);
		subject.setReplyListener(listener);
		subject.setVoteSessionMessages(new VotingSessionMessagesMock());
		return listener;
	}


	public ReplyListenerMock initializeProcessorWithVotingSessionAndGetListener(VotingCommandProcessorAbstract subject) {
		VOTING_POLL_COMMAND_PROCESSOR.makeNewVotingSession(buildVotingPollRequest());
		return initializeProcessorAndGetListener(subject);
	}

	public ChatBridgeMock getSessionChat() {
		if (chatBridgeMock == null)
			chatBridgeMock = new ChatBridgeMock("autoid");
		return (ChatBridgeMock) chatBridgeMock;
	}

	public VoteRequest makeVoteInInitializedChatSession(int vote) {
		return new VoteRequestMocked(getSessionChat(), vote);
	}

	public ReplyListenerMock initializeProcessorWithVotingSessionAnd1VoteAndGetListener(ClosePollProcessor subject) {
		ReplyListenerMock initializeProcessorWithVotingSessionAndGetListener = initializeProcessorWithVotingSessionAndGetListener(subject);
		getSessionChat().setLastSender("uruca");
		VOTING_POLL_COMMAND_PROCESSOR.processMessage(getSessionChat(), "#2");
		return initializeProcessorWithVotingSessionAndGetListener;
	}

	public ReplyListenerMock initializeProcessorWithVotingSessionWhereEveryoneVotedAndGetListener(MissingVotersProcessor subject) {
		ReplyListenerMock initializeProcessorWithVotingSessionAndGetListener = initializeProcessorWithVotingSessionAndGetListener(subject);
		getSessionChat().setLastSender("uruca");
		VOTING_POLL_COMMAND_PROCESSOR.processMessage(getSessionChat(), "#2");
		getSessionChat().setLastSender("tatu");
		VOTING_POLL_COMMAND_PROCESSOR.processMessage(getSessionChat(), "#2");
		return initializeProcessorWithVotingSessionAndGetListener;
	}


	public StartPollRequest buildVotingPollRequest() {
		chatBridgeMock = getSessionChat();
		StartPollRequest request = new StartPollRequest(chatBridgeMock);
		request.setWelcomeMessage("Almoço!");
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
		request.addParticipant("tatu");
		request.addParticipant("uruca");
		return request;
	}
}
