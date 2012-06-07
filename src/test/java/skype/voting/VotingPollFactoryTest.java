package skype.voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.VotingPollOption;
import skype.voting.VotingPollFactory;
import skype.voting.VotingPollVisitor;
import skype.voting.VotingPollRequest;

public class VotingPollFactoryTest {
	@Test
	public void onCorrectCommand_ShouldCreateLunchRequest() {
		VotingPollFactory subject = new VotingPollFactory();
		ChatBridgeMock chat = new ChatBridgeMock("#42");
		chat.addParticipant("joe");
		chat.addParticipant("moe");
		VotingPollRequest request = subject.produce(chat, "#startpoll verdinho,garbo");
		
		final StringBuffer sb= new StringBuffer();
		
		request.accept(
				new VotingPollVisitor() {
					public void visitOption(VotingPollOption option) {
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
