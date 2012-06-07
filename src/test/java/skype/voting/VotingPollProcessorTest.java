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

public class VotingPollProcessorTest {

	VotingSessionFactoryMock votingSessionFactoryMock = new VotingSessionFactoryMock();
	VotingPollProcessor subject = new VotingPollProcessor(votingSessionFactoryMock);
	ReplyListener listener;
	final AtomicReference<String> reply = new AtomicReference<String>();

	public VotingPollProcessorTest() {
		listener = new ReplyListener() {
			public void onReply(ChatAdapterInterface chat, String replyMessage) {
				reply.set(replyMessage);
			}
		};
		subject.addReplyListener(listener);
	}

	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu() {
		VotingSessionMock votingSessionMock = new VotingSessionMock();
		votingSessionFactoryMock.setMock(votingSessionMock);
		
		VotingPollRequest pollRequest = buildVotingPollRequest();
		assertEquals(votingSessionMock.pollRequest, pollRequest);

		String expected = "\n" + "Almoço!\n" + "1) foo\n" + "2) baz";
		assertEquals(expected, reply.get());
	}

	@Test
	public void onProcessVoteWithoutLunchSession_ShouldDoNothing() {
		votingSessionFactoryMock.setMock(new VotingSessionMockAdapter() {
			public void acceptVoteConsultant(VotingConsultant consultant) {/**/}
		});
		subject = new VotingPollProcessor(votingSessionFactoryMock);
		
		VoteRequest request = new VoteRequest("_foo_user_", 1);
		subject.processVoteRequest(request);
		assertNull(reply.get());
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		votingSessionFactoryMock.setMock(new VotingSessionMockAdapter() {
			public void acceptVoteConsultant(VotingConsultant consultant) {
				consultant.onVote(new VotingPollOption("foo"), 0);
				consultant.onVote(new VotingPollOption("baz"), 1);
			}
		});
		buildVotingPollRequest();

		VoteRequest voteRequest = new VoteRequest("_foo_user_", 2);
		subject.processVoteRequest(voteRequest);

		assertEquals("Votes: foo: 0 ; baz: 1", reply.get() + "");
	}

	private VotingPollRequest buildVotingPollRequest() {
		ChatBridgeMock chat = new ChatBridgeMock("autoid");

		VotingPollRequest request = new VotingPollRequest(chat);
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
		subject.processLunchRequest(request);
		return request;
	}

	@Test
	public void onProcessUnrecognizedCommand_ShouldReturnErrorMessage() {
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "#commandfoo"));
		String expected = "'#commandfoo' not recognized";
		assertEquals(expected, reply.get());
	}

	@Test
	public void onProcessUnrecognizedCommandWithoutLeadingSharp_ShouldDoNothing() {
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "commandfoo"));
		assertNull(reply.get());
	}

	private final class VotingSessionFactoryMock implements VotingSessionFactory {
		private VotingSession votingSessionMock = new VotingSessionMock();

		public VotingSession produce() {
			return votingSessionMock;
		}

		public void setMock(VotingSession votingSessionMock) {
			this.votingSessionMock = votingSessionMock;
		}
	}

	final class VotingSessionMock extends VotingSessionMockAdapter {

		@Override
		public void acceptVoteConsultant(VotingConsultant consultant) {
			throw new RuntimeException("NOT IMPLEMENTED");
		}
	}

}