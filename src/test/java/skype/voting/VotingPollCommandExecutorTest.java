package skype.voting;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.ReplyTextRequest;
import skype.shell.UnrecognizedCommand;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.application.VotingPollOption;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.mocks.VoteRequestMocked;
import skype.voting.mocks.VotingSessionMockAdapter;
import skype.voting.requests.AddVoteOptionRequest;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.MissingVotersRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VoteStatusRequest;

public class VotingPollCommandExecutorTest {

	VotingSessionFactoryMock votingSessionFactoryMock = new VotingSessionFactoryMock();
	ChatBridgeMock chatBridgeMock = new ChatBridgeMock("autoid");
	ReplyListenerMock listener;

	private VotingPollCommandExecutor getSubject() {
		VotingPollCommandExecutor subject = new VotingPollCommandExecutor(votingSessionFactoryMock);
		listener = new ReplyListenerMock();
		subject.setReplyListener(listener);
		return subject;
	}

	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu() {
		StartPollRequest pollRequest = buildVotingPollRequest();
		
		VotingPollCommandExecutor subject = getSubject();
		subject.processIfPossible(pollRequest);
		
		assertEquals(votingSessionFactoryMock.session.pollRequest, pollRequest);

		String expected = 
				"\n" + "Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n"  +
				"Voters: tatu,uruca\n";
		assertEquals(expected, listener.reply.get());
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
	public void onProcessVoteWithoutLunchSession_ShouldNotGenerateReply() {
		VotingPollCommandExecutor subject = getSubject();
		
		VoteRequest request = new VoteRequestMocked("_foo_user_", 1);
		subject.processIfPossible(request);
		assertEquals("", listener.reply.get());
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();

		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequestMocked(chatBridgeMock, 2);
		subject.processIfPossible(anyVoteRequestWillPrintCurrentResults);

		assertEquals("Votes: foo: 2 ; baz: 3", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueErrorIfVoteIsInvalid() {
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();

		VoteRequest invalidVote = new VoteRequestMocked(chatBridgeMock, 42);
		subject.processIfPossible(invalidVote);

		assertEquals("Error message here. Valid options:\n" +
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n" +
				"Voters: tatu,uruca\n", 
				listener.reply.get());
	}

	@Test
	public void onProcessVoteWithDifferentChatThanCurrentVoteRequest_ShouldNotIssueVote()
	{
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();

		ChatAdapterInterface chat2 = new ChatBridgeMock("anotherChat");
		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequestMocked(chat2 , 2);
		subject.processIfPossible(anyVoteRequestWillPrintCurrentResults);
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
		VotingPollCommandExecutor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		
		VoteRequest thisRequestShouldNotGenerateReply = new VoteRequestMocked("foo", 2);
		subject.processIfPossible(thisRequestShouldNotGenerateReply);
	}
	
	@Test
	public void onClosePollAfterClosedPoll_ShouldGenerateNoReply()
	{
		VotingPollCommandExecutor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		subject.processIfPossible(new ClosePollRequest(chatBridgeMock, null));
	}
	
	
	@Test
	public void onprocessIfPossible_ShouldReturnErrorMessage() {
		VotingPollCommandExecutor subject = getSubject();
		subject.processIfPossible(new UnrecognizedCommand(null, "#commandfoo"));
		String expected = "'#commandfoo' not recognized";
		assertEquals(expected, listener.replyPrivate.get());
	}

	@Test
	public void onprocessIfPossibleWithoutLeadingSharp_ShouldNotGenerateReply() {
		VotingPollCommandExecutor subject = getSubject();
		subject.processIfPossible(new UnrecognizedCommand(null, "commandfoo"));
		assertEquals("", listener.reply.get());
	}
	
	@Test
	public void onprocessIfPossible_ShouldPrintVotingStatusForGivenChat()
	{
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		VoteStatusRequest voteStatusRequest = new VoteStatusRequest(chatBridgeMock, null);
		subject.processIfPossible(voteStatusRequest);
		
		assertEquals("Votes: foo: 2 ; baz: 3", listener.replyPrivate.get() + "");
	}
	
	@Test
	public void onProcessMissingVotersRequest_ShouldPrintWhoHasnVoted()
	{
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		MissingVotersRequest request = new  MissingVotersRequest(chatBridgeMock, null);
		subject.processIfPossible(request);
		assertEquals("Users that haven't voted yet:\n" +
				"	tatu, uruca", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessMissingVotersRequestWithoutAnyone_ShouldPrintEveryoneVoted(){
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		votingSessionFactoryMock.session.setEveryoneVoted();
		MissingVotersRequest request = new  MissingVotersRequest(chatBridgeMock, null);
		subject.processIfPossible(request);
		assertEquals("Everyone already voted.", listener.reply.get() + "");
	}
	
	
	@Test
	public void onprocessIfPossibleWithClosedSession_ShouldNotGenerateReply()
	{
		VotingPollCommandExecutor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		VoteStatusRequest voteStatusRequest = new VoteStatusRequest(chatBridgeMock, null);
		subject.processIfPossible(voteStatusRequest);
	}
	
	@Test
	public void onProcessHelpRequest_ShouldGenerateReplyWithHelpMessage()
	{
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		HelpRequest request = new HelpRequest(chatBridgeMock, null){
			@Override
			public String getHelpMessage() {
				return "help";
			}
		};
		subject.processIfPossible(request);
		assertEquals("help\n", 
				listener.replyPrivate.get());
	}
	
	@Test
	public void onProcessHelpRequestWithouInitializedPoll_ShouldDoNothing()
	{
		VotingPollCommandExecutor subject = getSubject();
		subject.setReplyListener(getListenerThatBreaksIfInvoked());
		HelpRequest request = new HelpRequest(null, null);
		subject.processIfPossible(request);
	}
	
	@Test
	public void onprocessIfPossible_ShouldAddVotingOption(){
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "matre mia");
		chatBridgeMock.setLastSender("tatu");
		subject.processIfPossible(request);
		
		assertEquals("New option 'matre mia' added by tatu. Current options:\n" +
				"Almoço!\n" +
				"1) foo\n" + 
				"2) baz\n" +
				"3) matre mia\n"+
				"Voters: tatu,uruca", 
				listener.reply.get());
	}
	
	@Test
	public void onprocessIfPossibleThatAlreadyExists_ShouldNotAddAndShouldWarnUser()
	{
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "foo");
		chatBridgeMock.setLastSender("tatu");
		subject.processIfPossible(request);
		
		assertEquals("Option 'foo' already added.", listener.replyPrivate.get());
	}
	
	@Test
	public void onprocessIfPossibleWithClosedPoll_ShouldDoNothing(){
		VotingPollCommandExecutor subject = getSubjectWithClosedPollThatBreaksIfReplyListenerIsInvoked();
		AddVoteOptionRequest request = new AddVoteOptionRequest(chatBridgeMock, null, "matre mia");
		chatBridgeMock.setLastSender("tatu");
		subject.processIfPossible(request);
	}
	
	@Test
	public void onprocessIfPossible_ShouldReplyTextInRequest()
	{
		VotingPollCommandExecutor subject = getSubjectWithInitializedSession();
		ReplyTextRequest request = new ReplyTextRequest(chatBridgeMock, null, "some text");
		subject.processIfPossible(request);
		assertEquals("some text", listener.reply.get());
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


	final class ReplyListenerMock implements ReplyListener {
		final AtomicReference<String> replyPrivate = new AtomicReference<String>();
		final AtomicReference<String> reply = new AtomicReference<String>("");

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