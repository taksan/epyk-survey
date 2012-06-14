package skype.voting.requests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.application.VotingPollOption;

public class StartPollRequestTest  {
	@Test
	public void onAccept_ShouldPassAllOverRequiredFields()
	{
		StartPollRequest subject = getSubject();
		subject.setWelcomeMessage("WELCOME");
		subject.add(new VotingPollOption("1"));
		subject.add(new VotingPollOption("2"));
		subject.addParticipant("p1");
		subject.addParticipant("p2");
		
		final  StringBuilder actual = new StringBuilder();
		subject.accept(new VotingPollVisitor() {
			
			@Override
			public void onWelcomeMessage(String message) {
				accumulate(actual, message);
			}
			@Override
			public void visitOption(VotingPollOption option) {
				accumulate(actual,option.toString());
			}
			@Override
			public void visitParticipant(String participantName) {
				accumulate(actual, participantName);
			}
			private StringBuilder accumulate(final StringBuilder actual, String message) {
				return actual.append(message+"\n");
			}
		});
		String expected = "WELCOME\n" +
				"1\n" +
				"2\n" +
				"p1\n" +
				"p2\n";
		assertEquals(expected, actual.toString());
	}
	
	protected StartPollRequest getSubject() {
		return new StartPollRequest(null);
	}
}