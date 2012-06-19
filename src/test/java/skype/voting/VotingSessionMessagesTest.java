package skype.voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.mocks.VotingSessionMockAdapter;

public class VotingSessionMessagesTest { 
	VotingSessionMessages subject = new VotingSessionMessages();
	VotingSessionMockAdapter votingSession = new VotingSessionMockAdapter();
	
	@Test
	public void onGetVotingStatusMessage_ShouldBuildStatusMessage()
	{
		
		String actual = subject.getVotingStatusMessage(votingSession);
		
		assertEquals("foo: 2 ; baz: 3", actual);
	}
	
	@Test
	public void onBuildVotingMenu_ShouldBuildVotingMenu()
	{
		String actual = subject.buildVotingMenu(votingSession);
		
		String expected = "\n" + 
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n" + 
				"Voters: tatu,uruca\n";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void onBuildVotingMenuWithoutVoters_ShouldBuildVotingMenuWithoutVotersLine()
	{
		String actual = subject.buildVotingMenuWithoutVoters(votingSession);
		
		String expected = "\n" + 
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n" + 
				"\n";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void onGetUpdatedVotingMenu_ShouldBuildUpdatedMenu(){
		String actual = subject.getUpdatedVotingMenu(votingSession);
		
		String expected = "\n" + 
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n" + 
				"Voters: tatu,uruca\n";
		
		assertEquals(expected, actual);		
	}
}