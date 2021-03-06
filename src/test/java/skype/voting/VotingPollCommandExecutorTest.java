package skype.voting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.alias.mocks.HelpVisitorMock;
import skype.shell.AbstractShellCommand;
import skype.shell.ShellCommand;
import skype.shell.mocks.AliasExpanderMock;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.mocks.VotingCommandProcessorMock;
import skype.voting.mocks.VotingSessionMockAdapter;
import skype.voting.processors.requests.ClosePollRequest;
import skype.voting.processors.requests.StartPollRequest;

import com.skype.ChatListener;

public class VotingPollCommandExecutorTest {

	VotingSessionFactoryMock votingSessionFactoryMock = new VotingSessionFactoryMock();
	ChatBridgeMock chatBridgeMock = new ChatBridgeMock("autoid");
	ReplyListenerMock listener;
	VotingCommandProcessorMock processorMock = new VotingCommandProcessorMock();
	AliasExpanderMock expanderMock = new AliasExpanderMock();
	
	@Test
	public void onProcessMessage_ShouldInterpretAndProcess()
	{
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor(
				votingSessionFactoryMock, 
				new VotingSessionMessages(),
				processorMock
				);
		
		subject.processMessage(chatBridgeMock, "foo");
		
		assertEquals("foo", processorMock.processedMessage);
	}
	
	@Test
	public void onCreation_ShouldNotBeInitializedForAnyChat()
	{
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor();
		
		ShellCommand request = new AbstractShellCommand(chatBridgeMock, null) {	};
		assertFalse(subject.isInitializedSessionOnRequestChat(request));
	}
	
	@Test
	public void onMakeNewSession_ShouldAddListenerAndSession()
	{
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor();
		
		StartPollRequest votePollRequest = new StartPollRequest(chatBridgeMock);
		VotingSession newVotingSession = subject.makeNewVotingSession(votePollRequest);
		
		ChatListenerForVotingSession listenerForVotingSession = subject.listenersBySession.get(newVotingSession);
		
		assertEquals(chatBridgeMock.getListener(), listenerForVotingSession);
	}
	
	@Test
	public void onGetSessionForRequest_ShouldReturnGivenChatsSession() {
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor();
		
		StartPollRequest votePollRequest = new StartPollRequest(chatBridgeMock);
		VotingSession newVotingSession = subject.makeNewVotingSession(votePollRequest);
		
		ShellCommand request = new AbstractShellCommand(chatBridgeMock, null) {	};
		VotingSession sessionForRequest = subject.getSessionForRequest(request);
		
		assertEquals(newVotingSession, sessionForRequest);
	}
	
	@Test
	public void onRemoveSession_ShouldRemoveChatListenerFromChatAndBecomeNonInitializedForAnyRequest()
	{
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor();
		subject.listenersBySession.put(null, null);
		assertTrue(subject.listenersBySession.containsKey(null));
		
		ClosePollRequest request = new ClosePollRequest(chatBridgeMock, null);
		ChatListener absentListener = chatBridgeMock.getListener();
		chatBridgeMock.addListener(null);
		
		subject.removeSessionForGivenRequest(request);
		
		assertEquals(absentListener, chatBridgeMock.getListener());
		assertFalse(subject.listenersBySession.containsKey(null));
		
		ShellCommand someRequest = new AbstractShellCommand(chatBridgeMock, null) {	};
		assertFalse(subject.isInitializedSessionOnRequestChat(someRequest));
	}
	
	@Test
	public void onAcceptHelpVisitor_ShouldDelegateAcceptToAllProcessors()
	{
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor(
				votingSessionFactoryMock, 
				new VotingSessionMessages(),
				processorMock,
				processorMock
				);
		final StringBuilder operations = new StringBuilder();
		subject.acceptHelpVisitor(new HelpVisitorMock(operations));
		String expected = 
				"onTopLevel: Available voting commands\n" +
				"onTopic: VotingCommandProcessorMock visit\n"+
				"onTopic: VotingCommandProcessorMock visit\n";
		
		assertEquals(expected, operations.toString());
	}
	
	private final class VotingSessionFactoryMock implements VotingSessionFactory {
		private VotingSessionMockAdapter session;

		public VotingSession produce() {
			session = new VotingSessionMockAdapter();
			return session;
		}
	}
}