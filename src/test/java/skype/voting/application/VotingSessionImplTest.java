package skype.voting.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionImpl;
import skype.voting.mocks.VoteRequestMocked;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VotingPollVisitor;


public class VotingSessionImplTest {
	VotingSessionImpl subject = new VotingSessionImpl();
	public VotingSessionImplTest() {
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
		subject.vote(new VoteRequestMocked("john doe", 2));
		
		
		assertEquals("", getVotingTableFor(subject));
	}
	
	@Test
	public void onVote_ShouldCastToGivenOption() {
		subject.initWith(buildVotingPollRequest());
		final AtomicBoolean handled = new AtomicBoolean();
		subject.vote(new VoteRequestMocked("john doe", 2), new VoteFeedbackHandler() {
			@Override
			public void handleSuccess() {
				handled.set(true);
			}
			
			@Override
			public void handleError(String errorMessage) {
				throw new RuntimeException("Invalid vote not expected");
			}
		});
		assertTrue(handled.get());
		
		subject.vote(new VoteRequestMocked("jane doe", 1));
		
		String actual = getVotingTableFor(subject);
		String expected = 
				"foo:1\n" +
				"baz:1";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void onInvalidVote_ShouldReject()
	{
		final AtomicReference<String> error = new AtomicReference<String>("");
		subject.initWith(buildVotingPollRequest());
		subject.vote(new VoteRequestMocked("john doe", 3), new VoteFeedbackHandler() {
			@Override
			public void handleError(String errorMessage) {
				error.set(errorMessage);
			}

			@Override
			public void handleSuccess() {
				
			}
		});
		String actual = getVotingTableFor(subject);
		String expected = 
				"foo:0\n" +
				"baz:0";
		
		assertEquals(expected, actual);
		
		assertEquals("Invalid voting option 3", error.get());
	}
	
	@Test
	public void onAddNewParticipant_ShouldMakeItShowUpOnVisitor()
	{
		subject.initWith(buildVotingPollRequest());
		subject.addNewParticipant("raskell");
		String participants = subject.getParticipants();
		
		assertTrue(participants.contains("raskell"));
	}
	
	@Test
	public void onRemovePartipant_ShouldRemovePartipantVotesAndDeleteFromVisitor()
	{
		subject.initWith(buildVotingPollRequest());
		subject.vote(new VoteRequestMocked("john doe", 2));
		subject.removeParticipant("john doe");
		
		String actual = getVotingTableFor(subject);
		String expected = 
				"foo:0\n" +
				"baz:0";
		
		assertEquals(expected, actual);
		String participants = subject.getParticipants();
		
		assertFalse(participants.contains("john doe"));
	}
	
	@Test
	public void invokeAcceptVoteConsultantTwice_ShouldNotChangeVotingStatus()
	{
		subject.initWith(buildVotingPollRequest());
		subject.vote(new VoteRequestMocked("john doe", 2));
		getVotingTableFor(subject);
		getVotingTableFor(subject);
		String actual = getVotingTableFor(subject);
		String expected = 
				"foo:0\n" +
				"baz:1";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void onAddOption_ShouldAddNewOption() {
		subject.initWith(buildVotingPollRequest());
		boolean isAdded = subject.addOption("non non sense ");
		String actual = getVotingTableFor(subject);
		String expected = 
				"foo:0\n" + 
				"baz:0\n" + 
				"non non sense:0";
		assertEquals(expected, actual);
		assertTrue(isAdded);
	}
	
	@Test
	public void onAddAlreadyExistingOption_ShouldNotAddNewOption()
	{
		subject.initWith(buildVotingPollRequest());
		boolean isAdded = subject.addOption("bAz ");
		String actual = getVotingTableFor(subject);
		String expected = 
				"foo:0\n" + 
				"baz:0";
		assertEquals(expected, actual);
		assertFalse(isAdded);
	}
	
	@Test
	public void onAcceptWinnerConsultantInEmptySession_ShouldNotAcceptAnything()
	{
		VotingSessionImpl subject = new VotingSessionImpl();
		subject.acceptWinnerConsultant(new WinnerConsultant() {
			public void onWinner(VoteOptionAndCount winnerStats) {
				throw new IllegalStateException("Should not accept anything on uninitialized session");
			}
			public void onTie(Set<VotingPollOption> tiedOptions, int tieCount) {
				throw new IllegalStateException("Should not accept anything on uninitialized session");
			}
		});
	}
	
	@Test
	public void onAcceptWinnerConsultantInSessionWithoutVotes_ShouldSendNoWinner()
	{
		final AtomicReference<String> actual = new AtomicReference<String>("");
		subject.acceptWinnerConsultant(new WinnerConsultant() {
			@Override
			public void onWinner(VoteOptionAndCount winnerStats) {
				actual.set("This is wrong. Saying winner is " + winnerStats.toString());
			}

			@Override
			public void onTie(Set<VotingPollOption> tiedOptions, int tieCount) {
				String tied = StringUtils.join(tiedOptions,",");
				actual.set(tied+" "+tieCount+" votes"); 
			}
		});
		
		String expected="foo,baz 0 votes";
		assertEquals(expected, actual.get());
	}
	
	
	@Test
	public void onAcceptWinnerConsultant_ShouldReturnCurrentWinner()
	{
		subject.initWith(buildVotingPollRequest());
		subject.vote(new VoteRequestMocked("john doe", 2));
		
		final AtomicReference<VoteOptionAndCount> actual = new AtomicReference<VoteOptionAndCount>(); 
		subject.acceptWinnerConsultant(new WinnerConsultant() {
			public void onWinner(VoteOptionAndCount winnerStats) {
				actual.set(winnerStats);
			}
			public void onTie(Set<VotingPollOption> tiedOptions, int tieCount) {
				throw new IllegalStateException("This is not a tied poll");
			}
		});
		
		VoteOptionAndCount expected = new VoteOptionAndCount("baz", 1);
		assertEquals(expected.toString(), actual.toString());
	}
	
	@Test
	public void onAcceptVisitor_ShouldVisitInCorrectSequence()
	{
		subject.initWith(buildVotingPollRequest());
		final StringBuilder sb=new StringBuilder(); 
		subject.accept(new VotingPollVisitor() {
			@Override
			public void onWelcomeMessage(String message) {
				sb.append("onWelcomeMessage\n");
			}
			
			@Override
			public void visitOption(VotingPollOption option) {
				sb.append("visitOption:"+option+"\n");
			}
			
			@Override
			public void visitParticipant(String participantName) {
				sb.append("visitParticipant:"+participantName+"\n");
			}
		});
		
		String expected=
				"onWelcomeMessage\n"+
				"visitOption:foo\n"+
				"visitOption:baz\n"+
				"visitParticipant:john doe\n"+
				"visitParticipant:jane doe\n";
		assertEquals(expected, sb.toString());
	}
	
	@Test
	public void onAcceptParticipantConsultant_ShouldVisitAndTellWhetherUserVotedOrNot() {
		subject.initWith(buildVotingPollRequest());
		subject.vote(new VoteRequestMocked("john doe", 2));
		
		final StringBuilder sb = new StringBuilder();
		subject.acceptParticipantConsultant(new ParticipantConsultant() {
			
			@Override
			public void visit(String participantName, boolean hasVoted) {
				sb.append(participantName+":"+hasVoted+"\n");
			}
		});
		String expected = 
				"john doe:true\n" +
				"jane doe:false\n";
		
		assertEquals(expected, sb.toString());
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
	private StartPollRequest buildVotingPollRequest() {
		
		StartPollRequest request = new StartPollRequest(chat);
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
		request.addParticipant("john doe");
		request.addParticipant("jane doe");
		return request;
	}
	
	private StartPollRequest buildStartVotingRequest2() {
		StartPollRequest request = new StartPollRequest(chat);
		request.add(new VotingPollOption("mumbai"));
		request.add(new VotingPollOption("bombay"));
		request.addParticipant("homer");
		request.addParticipant("bart");
		return request;
	}

}