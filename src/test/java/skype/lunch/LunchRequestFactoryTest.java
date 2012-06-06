package skype.lunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;

public class LunchRequestFactoryTest {
	@Test
	public void onCorrectCommand_ShouldCreateLunchRequest() {
		LunchRequestFactory subject = new LunchRequestFactory();
		ChatBridgeMock chat = new ChatBridgeMock("#42");
		chat.addParticipant("joe");
		chat.addParticipant("moe");
		LunchRequest request = subject.produce(chat, "#lunch verdinho,garbo");
		
		final StringBuffer sb= new StringBuffer();
		
		request.accept(
				new LunchRequestVisitor() {
					public void visitOption(LunchOption option) {
						sb.append("Option: "+option.getName()+"\n");
					}

					@Override
					public void visitParticipant(String participantName) {
						sb.append("Participant: " + participantName+"\n");
					}
				});
		
		String expected = 
				"Option: verdinho\n" +
				"Option: garbo\n" +
				"Participant: joe\n" +
				"Participant: moe";
		assertEquals(expected, sb.toString().trim());
	}
}
