package taksan.lunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LunchShellTest {
	LunchShell subject = new LunchShell();
	
	@Test
	public void onAddParticipant_ShouldAddParticipant()
	{
		subject.addParticipant(new LunchParticipant("foosan"));
		subject.addParticipant(new LunchParticipant("foosan2"));
		
		ParticipantVisitor participantListener = new ParticipantVisitor();
		subject.accept(participantListener);
		
		String actual = participantListener.getListOfParticipants();
		String expected = 
				"foosan\n" +
				"foosan2";
		assertEquals(expected, actual);
	}
	
	@Test
	public void onLunchRequest_ShouldNotifyAllPartipants() {
		LunchParticipant lunchParticipant1 = new LunchParticipant("foosan");
		LunchParticipant lunchParticipant2 = new LunchParticipant("foosan2");
		
		subject.addParticipant(lunchParticipant1);
		subject.addParticipant(lunchParticipant2);
		
		subject.requestLunchDecision();
		
	}
}
