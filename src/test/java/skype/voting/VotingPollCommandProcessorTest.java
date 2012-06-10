package skype.voting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.UnrecognizedCommand;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.mocks.VotingSessionMockAdapter;
import skype.voting.requests.AddVoteOptionRequest;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VoteStatusRequest;

public class VotingPollCommandProcessorTest {

	VotingSessionFactoryMock votingSessionFactoryMock = new VotingSessionFactoryMock();
	ChatBridgeMock chatBridgeMock = new ChatBridgeMock("autoid");
	ReplyListenerMock listener;

	private VotingPollCommandProcessor getSubject() {
		VotingPollCommandProcessor subject = new VotingPollCommandProcessor(votingSessionFactoryMock);
		listener = new ReplyListenerMock();
		subject.addReplyListener(listener);
		return subject;
	}

	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu() {
		StartPollRequest pollRequest = buildVotingPollRequest();
		
		VotingPollCommandProcessor subject = getSubject();
		subject.processVotingPollRequest(pollRequest);
		
		assertEquals(votingSessionFactoryMock.session.pollRequest, pollRequest);

		String expected = 
				"\n" + "Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n"  +
				"Voters: tatu,uruca\n";
		assertEquals(expected, listener.reply.get());
		assertEquals("Poll 'Almoço!' undergoing. Options: 1) foo 2) baz", 
				chatBridgeMock.getLastSentGuidelines());
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
	public void onProcessVoteWithoutLunchSession_ShouldNotGenerateReply() {
		VotingPollCommandProcessor subject = getSubject();
		
		VoteRequest request = new VoteRequest("_foo_user_", 1);
		subject.processVoteRequest(request);
		assertNull(listener.reply.get());
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();

		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequest(chatBridgeMock, null, 42);
		subject.processVoteRequest(anyVoteRequestWillPrintCurrentResults);

		assertEquals("Votes: foo: 2 ; baz: 3", listener.reply.get() + "");
	}

	@Test
	public void onProcessVoteWithDifferentChatThanCurrentVoteRequest_ShouldNotIssueVote()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();

		ChatAdapterInterface chat2 = new ChatBridgeMock("anotherChat");
		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequest(chat2 , null, 42);
		subject.processVoteRequest(anyVoteRequestWillPrintCurrentResults);
	}

	@Test
	public void onClosePollRequest_ShouldPrintTheResults()
	{
		getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		assertEquals(
				"Votes: foo: 2 ; baz: 3\n" +
				"WINNER: ***baz*** with 3 votes", 
				listener.reply.get());
	}

	@Test
	public void onClosePollRequestWithTie_ShouldPrintTieResults()
	{
		votingSessionFactoryMock.forceTie();
		getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		assertEquals(
				"Votes: foo: 3 ; baz: 3\n" +
				"TIE: **foo and baz** tied with 3 votes", 
				listener.reply.get());
	}
	
	@Test
	public void processVoteAfterClosePollRequest_ShouldGenerateNoReply(){
		VotingPollCommandProcessor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		
		VoteRequest thisRequestShouldNotGenerateReply = new VoteRequest("foo", 42);
		subject.processVoteRequest(thisRequestShouldNotGenerateReply);
	}
	
	@Test
	public void onClosePollAfterClosedPoll_ShouldGenerateNoReply()
	{
		VotingPollCommandProcessor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		subject.processClosePollRequest(new ClosePollRequest(chatBridgeMock, null));
	}
	
	
	@Test
	public void onProcessUnrecognizedCommand_ShouldReturnErrorMessage() {
		VotingPollCommandProcessor subject = getSubject();
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "#commandfoo"));
		String expected = "'#commandfoo' not recognized";
		assertEquals(expected, listener.reply.get());
	}

	@Test
	public void onProcessUnrecognizedCommandWithoutLeadingSharp_ShouldNotGenerateReply() {
		VotingPollCommandProcessor subject = getSubject();
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "commandfoo"));
		assertNull(listener.reply.get());
	}
	
	@Test
	public void onProcessVoteStatusRequest_ShouldPrintVotingStatusForGivenChat()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		VoteStatusRequest voteStatusRequest = new VoteStatusRequest(chatBridgeMock, null);
		subject.processVoteStatusRequest(voteStatusRequest);
		
		assertEquals("Votes: foo: 2 ; baz: 3", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessVoteStatusRequestWithClosedSession_ShouldNotGenerateReply()
	{
		VotingPollCommandProcessor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		VoteStatusRequest voteStatusRequest = new VoteStatusRequest(chatBridgeMock, null);
		subject.processVoteStatusRequest(voteStatusRequest);
	}
	
	@Test
	public void onProcessHelpRequest_ShouldGenerateReplyWithHelpMessage()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		ShellCommand request = new HelpRequest(chatBridgeMock, null){
			@Override
			public String getHelpMessage() {
				return "help";
			}
		};
		request.beProcessedAsReceivedMessage(subject);
		assertEquals("help\n" +
				"Also, you can use '/get guidelines' to see the available voting options", 
				listener.reply.get());
	}
	
	@Test
	public void onProcessHelpRequestWithouInitializedPoll_ShouldDoNothing()
	{
		VotingPollCommandProcessor subject = getSubject();
		subject.addReplyListener(getListenerThatBreaksIfInvoked());
		ShellCommand request = new HelpRequest(null, null);
		request.beProcessedAsReceivedMessage(subject);
	}
	
	@Test
	public void onProcessAddVoteOption_ShouldAddVotingOption(){
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "matre mia");
		chatBridgeMock.setLastSender("tatu");
		request.beProcessedAsReceivedMessage(subject);
		
		assertEquals("New option 'matre mia' added by tatu. Current options:\n" +
				"Almoço!\n" +
				"1) foo\n" + 
				"2) baz\n" +
				"3) matre mia\n"+
				"Voters: tatu,uruca", 
				listener.reply.get());
	}
	
	@Test
	public void onProcessAddVoteOptionWithClosedPoll_ShouldDoNothing(){
		VotingPollCommandProcessor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "matre mia");
		chatBridgeMock.setLastSender("tatu");
		request.beProcessedAsReceivedMessage(subject);
	}
	
	@Test
	public void onProcessAddVoteOptionThatAlreadyExists_ShouldNotAddAndShouldWarnUser()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "foo");
		chatBridgeMock.setLastSender("tatu");
		request.beProcessedAsReceivedMessage(subject);
		
		assertEquals("Option 'foo' already added.", listener.reply.get());
	}
	
	private VotingPollCommandProcessor getSubjectWithInitializedSession() {
		VotingPollCommandProcessor subject = getSubject();
		subject.processVotingPollRequest(buildVotingPollRequest());
		return subject;
	}	

	private VotingPollCommandProcessor getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked() {
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		subject.processClosePollRequest(new ClosePollRequest(chatBridgeMock, null));
		subject.addReplyListener(getListenerThatBreaksIfInvoked());
		return subject;
	}	
	
	private ReplyListener getListenerThatBreaksIfInvoked() {
		return new ReplyListener() {
			@Override
			public void onReply(ChatAdapterInterface chatAdapterInterface, String reply) {
				throw new RuntimeException("Should generate no reply");
			}
		};
	}


	final class ReplyListenerMock implements ReplyListener {
		final AtomicReference<String> reply = new AtomicReference<String>();

		public void onReply(ChatAdapterInterface chat, String replyMessage) {
			reply.set(replyMessage);
		}
	}

	private final class VotingSessionFactoryMock implements VotingSessionFactory {
		private VotingSessionMockAdapter session;
		private boolean isTie;

		public VotingSession produce() {
			session = new VotingSessionMockAdapter(isTie);
			return session;
		}

		public void forceTie() {
			isTie = true;
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