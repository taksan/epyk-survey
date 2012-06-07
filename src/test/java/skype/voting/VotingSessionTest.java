package skype.voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.VotingPollOption;
import skype.voting.VotingPollRequest;
import skype.voting.VoteRequest;
import skype.voting.VotingConsultant;
import skype.voting.VotingSessionImpl;


public class VotingSessionTest {
	VotingSessionImpl subject = new VotingSessionImpl();
	public VotingSessionTest() {
		subject.initWith(buildVotingPollRequest());
	}
	
	@Test
	public void onStartVotingRequest_ShouldAddOptions()
	{
		String actual = subject.getVoteOptions();
		assertEquals("foo,baz", actual);
	}

	@Test
	public void onLunchRequest_ShouldAddParticipants()
	{
		String actual = subject.getParticipants();
		assertEquals("john doe,jane doe", actual);
	}
	
	@Test
	public void onNewLunchRequest_ShouldKeepOnlyNewRequest(){
		subject.initWith(buildVotingPollRequest());
		subject.initWith(buildStartVotingRequest2());
		
		assertEquals("mumbai,bombay", subject.getVoteOptions());
		assertEquals("homer,bart", subject.getParticipants());
	}
	
	@Test
	public void onVoteWithoutSession_ShouldDoNothing()
	{
		VotingSession subject = new VotingSessionImpl();
		subject.vote(new VoteRequest("john doe", 2));
		
		
		assertEquals("", getVotingTableFor(subject));
	}
	
	@Test
	public void onVote_ShouldCastToGivenOption() {
		subject.initWith(buildVotingPollRequest());
		subject.vote(new VoteRequest("john doe", 2));
		String actual = getVotingTableFor(subject);
		String expected = 
				"foo:0\n" +
				"baz:1";
		
		assertEquals(expected, actual);
	}
	
	private String getVotingTableFor(VotingSession session) {
		final StringBuilder sb = new StringBuilder();
		session.acceptVoteConsultant(new VotingConsultant() {
			@Override
			public void onVote(VotingPollOption option, Integer count) {
				sb.append(option.getName()+":"+count+"\n");
			}
		});
		return sb.toString().trim();
	}

	ChatBridgeMock chat = new ChatBridgeMock("autoid");
	private VotingPollRequest buildVotingPollRequest() {
		
		VotingPollRequest request = new VotingPollRequest(chat);
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
		request.addParticipant("john doe");
		request.addParticipant("jane doe");
		return request;
	}
	
	private VotingPollRequest buildStartVotingRequest2() {
		VotingPollRequest request = new VotingPollRequest(chat);
		request.add(new VotingPollOption("mumbai"));
		request.add(new VotingPollOption("bombay"));
		request.addParticipant("homer");
		request.addParticipant("bart");
		return request;
	}

}