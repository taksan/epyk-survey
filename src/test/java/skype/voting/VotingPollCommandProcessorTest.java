package skype.voting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.ReplyTextRequest;
import skype.shell.UnrecognizedCommand;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.mocks.VoteRequestMocked;
import skype.voting.mocks.VotingSessionMockAdapter;
import skype.voting.requests.AddVoteOptionRequest;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.MissingVotersRequest;
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
		
		VoteRequest request = new VoteRequestMocked("_foo_user_", 1);
		subject.processVoteRequest(request);
		assertNull(listener.reply.get());
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();

		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequestMocked(chatBridgeMock, 2);
		subject.processVoteRequest(anyVoteRequestWillPrintCurrentResults);

		assertEquals("Votes: foo: 2 ; baz: 3", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueErrorIfVoteIsInvalid() {
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();

		VoteRequest invalidVote = new VoteRequestMocked(chatBridgeMock, 42);
		subject.processVoteRequest(invalidVote);

		assertEquals("Invalid voting option 42. Valid options:\n" +
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n", 
				listener.reply.get() + "");
	}

	@Test
	public void onProcessVoteWithDifferentChatThanCurrentVoteRequest_ShouldNotIssueVote()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();

		ChatAdapterInterface chat2 = new ChatBridgeMock("anotherChat");
		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequestMocked(chat2 , 2);
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
		
		VoteRequest thisRequestShouldNotGenerateReply = new VoteRequestMocked("foo", 2);
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
		assertEquals(expected, listener.replyPrivate.get());
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
		
		assertEquals("Votes: foo: 2 ; baz: 3", listener.replyPrivate.get() + "");
	}
	
	@Test
	public void onProcessMissingVotersRequest_ShouldPrintWhoHasnVoted()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		MissingVotersRequest request = new  MissingVotersRequest(chatBridgeMock, null);
		subject.processMissingVoteRequest(request);
		assertEquals("Users that haven't voted yet:\n" +
				"	tatu, uruca", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessMissingVotersRequestWithoutAnyone_ShouldPrintEveryoneVoted(){
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		votingSessionFactoryMock.session.setEveryoneVoted();
		MissingVotersRequest request = new  MissingVotersRequest(chatBridgeMock, null);
		subject.processMissingVoteRequest(request);
		assertEquals("Everyone already voted.", listener.reply.get() + "");
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
		HelpRequest request = new HelpRequest(chatBridgeMock, null){
			@Override
			public String getHelpMessage() {
				return "help";
			}
		};
		subject.processHelpCommand(request);
		assertEquals("help\n" +
				"Also, you can use '/get guidelines' to see the available voting options", 
				listener.replyPrivate.get()+"");
	}
	
	@Test
	public void onProcessHelpRequestWithouInitializedPoll_ShouldDoNothing()
	{
		VotingPollCommandProcessor subject = getSubject();
		subject.addReplyListener(getListenerThatBreaksIfInvoked());
		HelpRequest request = new HelpRequest(null, null);
		subject.processHelpCommand(request);
	}
	
	@Test
	public void onProcessAddVoteOption_ShouldAddVotingOption(){
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "matre mia");
		chatBridgeMock.setLastSender("tatu");
		subject.processAddVoteOption(request);
		
		assertEquals("New option 'matre mia' added by tatu. Current options:\n" +
				"Almoço!\n" +
				"1) foo\n" + 
				"2) baz\n" +
				"3) matre mia\n"+
				"Voters: tatu,uruca", 
				listener.reply.get());
	}
	
	@Test
	public void onProcessAddVoteOptionThatAlreadyExists_ShouldNotAddAndShouldWarnUser()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "foo");
		chatBridgeMock.setLastSender("tatu");
		subject.processAddVoteOption(request);
		
		assertEquals("Option 'foo' already added.", listener.replyPrivate.get());
	}
	
	@Test
	public void onProcessAddVoteOptionWithClosedPoll_ShouldDoNothing(){
		VotingPollCommandProcessor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "matre mia");
		chatBridgeMock.setLastSender("tatu");
		subject.processAddVoteOption(request);
	}
	
	@Test
	public void onProcessReplyTextRequest_ShouldReplyTextInRequest()
	{
		VotingPollCommandProcessor subject = getSubjectWithInitializedSession();
		ReplyTextRequest request = new ReplyTextRequest(chatBridgeMock, null, "some text");
		subject.processReplyTextRequest(request);
		assertEquals("some text", listener.reply.get());
	}
	
	@Test
	public void onProcessReplyTextRequestWithoutInitializedSession_ShouldDoNothing()
	{
		VotingPollCommandProcessor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		ReplyTextRequest request = new ReplyTextRequest(chatBridgeMock, null, "some text");
		subject.processReplyTextRequest(request);
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

			@Override
			public void onReplyPrivate(ChatAdapterInterface chatAdapterInterface, String reply) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
		};
	}


	final class ReplyListenerMock implements ReplyListener {
		final AtomicReference<String> replyPrivate = new AtomicReference<String>();
		final AtomicReference<String> reply = new AtomicReference<String>();

		public void onReply(ChatAdapterInterface chat, String replyMessage) {
			reply.set(replyMessage);
		}

		@Override
		public void onReplyPrivate(ChatAdapterInterface chatAdapterInterface, String reply) {
			replyPrivate.set(reply);
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