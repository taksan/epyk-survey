package skype.lunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatId;

public class LunchRequestFactoryTest {
	@Test
	public void onCorrectCommand_ShouldCreateLunchRequest() {
		LunchRequestFactory subject = new LunchRequestFactory();
		LunchRequest message = subject.produce(new ChatId("#42"), "#lunch verdinho,garbo");
		
		final StringBuffer sb= new StringBuffer();
		
		message.accept(
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
