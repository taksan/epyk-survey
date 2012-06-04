package skype.lunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;

public class LunchRequestFactoryTest {
	@Test
	public void onCorrectCommand_ShouldCreateLunchRequest() {
		LunchRequestFactory subject = new LunchRequestFactory();
		ChatBridgeMock chat = new ChatBridgeMock("#42");
		LunchRequest request = subject.produce(chat, "#lunch verdinho,garbo");
		
		final StringBuffer sb= new StringBuffer();
		
		request.accept(
				new LunchRequestVisitor() {
					public void visit(LunchOption option) {
						sb.append(option.getName()+"\n");
					}
				});
		
		String expected = 
				"verdinho\n" +
				"garbo";
		assertEquals(expected, sb.toString().trim());
	}
}
