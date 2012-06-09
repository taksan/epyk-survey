package skype.voting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.mocks.VotingSessionMockAdapter;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public class VotingPollProcessorTest {

	VotingSessionFactoryMock votingSessionFactoryMock = new VotingSessionFactoryMock();
	ChatBridgeMock chatBridgeMock = new ChatBridgeMock("autoid");
	ReplyListenerMock listener;

	private VotingPollProcessor getSubject() {
		VotingPollProcessor subject = new VotingPollProcessor(votingSessionFactoryMock);
		listener = new ReplyListenerMock();
		subject.addReplyListener(listener);
		return subject;
	}

	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu() {
		VotingPollRequest pollRequest = buildVotingPollRequest();
		
		VotingPollProcessor subject = getSubject();
		subject.processVotingPollRequest(pollRequest);
		
		assertEquals(votingSessionFactoryMock.session.pollRequest, pollRequest);

		String expected = 
				"\n" + "Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n"  +
				"Voters: tatu,uruca";
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

		assertEquals("User 'gamba' added to the voting poll.", listener.reply.get());
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
		VotingPollProcessor subject = buildClosedPoll();
		subject.addReplyListener(getListenerThatBreaksIfInvoked());
		
		chatBridgeMock.removeParticipant("tatu");
	}

	@Test
	public void onProcessVoteWithoutLunchSession_ShouldDoNothing() {
		VotingPollProcessor subject = getSubject();
		
		VoteRequest request = new VoteRequest("_foo_user_", 1);
		subject.processVoteRequest(request);
		assertNull(listener.reply.get());
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		VotingPollProcessor subject = getSubjectWithInitializedSession();

		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequest(chatBridgeMock, null, 42);
		subject.processVoteRequest(anyVoteRequestWillPrintCurrentResults);

		assertEquals("Votes: foo: 2 ; baz: 3", listener.reply.get() + "");
	}

	@Test
	public void onProcessVoteWithDifferentChatThanCurrentVoteRequest_ShouldNotIssueVote()
	{
		VotingPollProcessor subject = getSubjectWithInitializedSession();
		
		subject.addReplyListener(getListenerThatBreaksIfInvoked());

		ChatAdapterInterface chat2 = new ChatBridgeMock("anotherChat");
		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequest(chat2 , null, 42);
		subject.processVoteRequest(anyVoteRequestWillPrintCurrentResults);
	}

	private ReplyListener getListenerThatBreaksIfInvoked() {
		return new ReplyListener() {
			@Override
			public void onReply(ChatAdapterInterface chatAdapterInterface, String reply) {
				throw new RuntimeException("Should generate no reply");
			}
		};
	}

	@Test
	public void onClosePollRequest_ShouldPrintTheResults()
	{
		buildClosedPoll();
		assertEquals(
				"Votes: foo: 2 ; baz: 3\n" +
				"WINNER: ***baz*** with 3 votes", 
				listener.reply.get());
	}

	@Test
	public void onClosePollRequestWithTie_ShouldPrintTieResults()
	{
		votingSessionFactoryMock.forceTie();
		buildClosedPoll();
		assertEquals(
				"Votes: foo: 3 ; baz: 3\n" +
				"TIE: **foo and baz** tied with 3 votes", 
				listener.reply.get());
	}
	
	@Test
	public void processVoteAfterClosePollRequest_ShouldGenerateNoReply(){
		VotingPollProcessor subject = buildClosedPoll();
		subject.addReplyListener(getListenerThatBreaksIfInvoked());
		
		VoteRequest thisRequestShouldDoNothing = new VoteRequest("foo", 42);
		subject.processVoteRequest(thisRequestShouldDoNothing);
	}
	
	@Test
	public void onClosePollAfterClosedPoll_ShouldGenerateNoReply()
	{
		VotingPollProcessor subject = buildClosedPoll();
		subject.addReplyListener(getListenerThatBreaksIfInvoked());
		subject.processClosePollRequest(new ClosePollRequest(chatBridgeMock, null));
	}
	
	
	@Test
	public void onProcessUnrecognizedCommand_ShouldReturnErrorMessage() {
		VotingPollProcessor subject = getSubject();
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "#commandfoo"));
		String expected = "'#commandfoo' not recognized";
		assertEquals(expected, listener.reply.get());
	}

	@Test
	public void onProcessUnrecognizedCommandWithoutLeadingSharp_ShouldDoNothing() {
		VotingPollProcessor subject = getSubject();
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "commandfoo"));
		assertNull(listener.reply.get());
	}
	
	private VotingPollProcessor getSubjectWithInitializedSession() {
		VotingPollProcessor subject = getSubject();
		subject.processVotingPollRequest(buildVotingPollRequest());
		return subject;
	}	

	private VotingPollProcessor buildClosedPoll() {
		VotingPollProcessor subject = getSubjectWithInitializedSession();
		subject.processClosePollRequest(new ClosePollRequest(chatBridgeMock, null));
		return subject;
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


	private VotingPollRequest buildVotingPollRequest() {

		VotingPollRequest request = new VotingPollRequest(chatBridgeMock);
		request.setWelcomeMessage("Almoço!");
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
		request.addParticipant("tatu");
		request.addParticipant("uruca");
		return request;
	}
}