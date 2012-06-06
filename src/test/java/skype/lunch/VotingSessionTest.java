package skype.lunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;


public class VotingSessionTest {
	VotingSession subject = new VotingSession();
	public VotingSessionTest() {
		subject.initWith(buildLunchRequest());
	}
	
	@Test
	public void onLunchRequest_ShouldAddOptions()
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
		subject.initWith(buildLunchRequest());
		subject.initWith(buildLunchRequest2());
		
		assertEquals("mumbai,bombay", subject.getVoteOptions());
		assertEquals("homer,bart", subject.getParticipants());
	}
	
	@Test
	public void onVoteWithoutSession_ShouldDoNothing()
	{
		VotingSession subject = new VotingSession();
		subject.vote(new VoteRequest("john doe", 2));
		assertEquals("", subject.getVotingTable());
	}
	
	@Test
	public void onVote_ShouldCastToGivenOption() {
		subject.initWith(buildLunchRequest());
		subject.vote(new VoteRequest("john doe", 2));
		String actual = subject.getVotingTable();
		String expected = 
				"foo:0\n" +
				"baz:1";
		
		assertEquals(expected, actual);
	}
	
	ChatBridgeMock chat = new ChatBridgeMock("autoid");
	private LunchRequest buildLunchRequest() {
		
		LunchRequest request = new LunchRequest(chat);
		request.add(new LunchOption("foo"));
		request.add(new LunchOption("baz"));
		request.addParticipant("john doe");
		request.addParticipant("jane doe");
		return request;
	}
	
	private LunchRequest buildLunchRequest2() {
		LunchRequest request = new LunchRequest(chat);
		request.add(new LunchOption("mumbai"));
		request.add(new LunchOption("bombay"));
		request.addParticipant("homer");
		request.addParticipant("bart");
		return request;
	}

}