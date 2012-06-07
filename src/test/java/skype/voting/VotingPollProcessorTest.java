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
	ReplyListener listener;
	final AtomicReference<String> reply = new AtomicReference<String>();

	private VotingPollProcessor getSubject() {
		VotingPollProcessor subject = new VotingPollProcessor(votingSessionFactoryMock);
		listener = new ReplyListener() {
			public void onReply(ChatAdapterInterface chat, String replyMessage) {
				reply.set(replyMessage);
			}
		};
		subject.addReplyListener(listener);
		return subject;
	}

	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu() {
		VotingPollRequest pollRequest = buildVotingPollRequest();
		
		VotingPollProcessor subject = getSubject();
		subject.processLunchRequest(pollRequest);
		
		assertEquals(votingSessionFactoryMock.session.pollRequest, pollRequest);

		String expected = "\n" + "Almoço!\n" + "1) foo\n" + "2) baz";
		assertEquals(expected, reply.get());
	}

	@Test
	public void onProcessVoteWithoutLunchSession_ShouldDoNothing() {
		VotingPollProcessor subject = getSubject();
		
		VoteRequest request = new VoteRequest("_foo_user_", 1);
		subject.processVoteRequest(request);
		assertNull(reply.get());
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		VotingPollProcessor subject = getSubjectWithInitializedSession();

		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequest(null, 42);
		subject.processVoteRequest(anyVoteRequestWillPrintCurrentResults);

		assertEquals("Votes: foo: 2 ; baz: 3", reply.get() + "");
	}

	@Test
	public void onClosePollRequest_ShouldPrintTheResults()
	{
		buildClosedPoll();
		assertEquals(
				"Votes: foo: 2 ; baz: 3\n" +
				"WINNER: ***baz*** with 3 votes", 
				reply.get());
	}

	@Test
	public void onClosePollRequestWithTie_ShouldPrintTieResults()
	{
		votingSessionFactoryMock.forceTie();
		buildClosedPoll();
		assertEquals(
				"Votes: foo: 3 ; baz: 3\n" +
				"TIE: **foo and baz** tied with 3 votes", 
				reply.get());
	}
	
	@Test
	public void processVoteAfterClosePollRequest_ShouldGenerateNoReply(){
		VotingPollProcessor subject = buildClosedPoll();
		reply.set(null);
		
		VoteRequest thisRequestShouldDoNothing = new VoteRequest("foo", 42);
		subject.processVoteRequest(thisRequestShouldDoNothing);
		assertNull(reply.get());
	}
	
	@Test
	public void onClosePollAfterClosedPoll_ShouldGenerateNoReply()
	{
		VotingPollProcessor subject = buildClosedPoll();
		reply.set(null);
		subject.processClosePollRequest(null);
		assertNull(reply.get());
	}
	
	
	@Test
	public void onProcessUnrecognizedCommand_ShouldReturnErrorMessage() {
		VotingPollProcessor subject = getSubject();
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "#commandfoo"));
		String expected = "'#commandfoo' not recognized";
		assertEquals(expected, reply.get());
	}

	@Test
	public void onProcessUnrecognizedCommandWithoutLeadingSharp_ShouldDoNothing() {
		VotingPollProcessor subject = getSubject();
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "commandfoo"));
		assertNull(reply.get());
	}
	
	private VotingPollProcessor getSubjectWithInitializedSession() {
		VotingPollProcessor subject = getSubject();
		subject.processLunchRequest(buildVotingPollRequest());
		return subject;
	}	

	private VotingPollProcessor buildClosedPoll() {
		VotingPollProcessor subject = getSubjectWithInitializedSession();
		subject.processClosePollRequest(new ClosePollRequest(null, null));
		return subject;
	}	

	private final class VotingSessionFactoryMock implements VotingSessionFactory {
		public VotingSessionMockAdapter session;
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
		ChatBridgeMock chat = new ChatBridgeMock("autoid");

		VotingPollRequest request = new VotingPollRequest(chat);
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
		return request;
	}
}