package skype.voting;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.CommandInterpreter;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.application.VotingPollOption;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.mocks.ShellCommandMock;
import skype.voting.mocks.VotingSessionMockAdapter;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.StartPollRequest;

public class VotingPollCommandExecutorTest {

	VotingSessionFactoryMock votingSessionFactoryMock = new VotingSessionFactoryMock();
	ChatBridgeMock chatBridgeMock = new ChatBridgeMock("autoid");
	ReplyListenerMock listener;

	private VotingPollCommandExecutor getSubject() {
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor(votingSessionFactoryMock, new VotingSessionMessages(), 
				null);
		listener = new ReplyListenerMock();
		subject.setReplyListener(listener);
		return subject;
	}
	
	@Test
	public void onProcessMessage_ShouldInterpretAndProcess()
	{
		final ShellCommandMock commandToSend = new ShellCommandMock();
		final AtomicReference<ShellCommand> commandToProcess=new AtomicReference<ShellCommand>();
		final CommandInterpreter interpreter = 
			new CommandInterpreter() {
				@Override
				public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
					if (message.equals("foo"))
						return commandToSend;
					return null;
				}
		};
		
		VotingCommandProcessor[] commands = new VotingCommandProcessor[]{
				new VotingCommandProcessorAbstract() {
					
					@Override
					public void process(ShellCommand command) {
						commandToProcess.set(command);
					}
					
					@Override
					public boolean canProcess(ShellCommand command) {
						return command.equals(commandToSend);
					}
				}
		};
		
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor(
				votingSessionFactoryMock, 
				new VotingSessionMessages(),
				interpreter,
				commands
				);
		subject.processMessage(chatBridgeMock, "foo");
		assertEquals(commandToSend, commandToProcess.get());
	}
	
	@Test
	public void onNewUserOnGivenChat_ShouldAddUserToVotingSession()
	{
		getSubjectWithInitializedSession();
		
		assertEquals("tatu,uruca", votingSessionFactoryMock.session.getParticipants());
		
		chatBridgeMock.addParticipant("gamba");
		
		assertEquals("tatu,uruca,gamba", votingSessionFactoryMock.session.getParticipants());
		votingSessionFactoryMock.session.getParticipants();

		assertEquals(
				"User 'gamba' added to the voting poll.\n" +
				"Votes: foo: 2 ; baz: 3", 
				listener.reply.get());
		
		assertEquals(
				"Hey, we are having a voting poll. Come and join us. Here are the options:\n" + 
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n" +  
				"Vote by using #1,#2, and so on",
				listener.replyPrivate.get());
	}
	
	@Test
	public void onUserLeftOnGiveChat_ShouldRemoveFromVotingSession()
	{
		getSubjectWithInitializedSession();
		
		chatBridgeMock.removeParticipant("tatu");
		
		assertEquals("uruca", votingSessionFactoryMock.session.getParticipants());
		assertEquals(
				"User 'tatu' left the voting poll.\n" +
				"Update Votes: foo: 2 ; baz: 3", 
				listener.reply.get());
	}
	
	@Test
	public void onUserLeftOnGivenChatAfterClosedPoll_NothingShouldHappen()
	{
		getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		chatBridgeMock.removeParticipant("tatu");
	}
	
	@Test
	public void onProcessHelpRequestWithouInitializedPoll_ShouldDoNothing()
	{
		VotingPollCommandExecutor subject = getSubject();
		subject.setReplyListener(getListenerThatBreaksIfInvoked());
		HelpRequest request = new HelpRequest(null, null);
		subject.processIfPossible(request);
	}
	
	private VotingPollCommandExecutor getSubjectWithInitializedSession() {
		VotingPollCommandExecutor subject = getSubject();
		subject.processIfPossible(buildVotingPollRequest());
		return subject;
	}	

	private VotingPollCommandExecutor getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked() {
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		subject.processIfPossible(new ClosePollRequest(chatBridgeMock, null));
		subject.setReplyListener(getListenerThatBreaksIfInvoked());
		return subject;
	}	
	
	private ReplyListener getListenerThatBreaksIfInvoked() {
		return new ReplyListener() {
			@Override
			public void onReply(ChatAdapterInterface chatAdapterInterface, String reply) {
				throw new RuntimeException("Should generate no reply");
			}

			@Override
			public void onReplyPrivate(ChatAdapterInterface chatAdapterInterface, String reply) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
		};
	}


	private final class VotingSessionFactoryMock implements VotingSessionFactory {
		private VotingSessionMockAdapter session;
		private boolean isTie;

		public VotingSession produce() {
			session = new VotingSessionMockAdapter(isTie);
			return session;
		}
	}

	private StartPollRequest buildVotingPollRequest() {
		StartPollRequest request = new StartPollRequest(chatBridgeMock);
		request.setWelcomeMessage("Almoço!");
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
		request.addParticipant("tatu");
		request.addParticipant("uruca");
		return request;
	}
}