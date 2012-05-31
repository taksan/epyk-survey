package skype.lunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.lunch.mocks.SkypeBridgeMock;


public class LunchShellTest {
	@Test
	public void onSentLunchRequest_ShouldNotifyGroup() {
		SkypeBridgeMock skypeBridge = new SkypeBridgeMock();
		
		LunchShell subject = new LunchShell(null, skypeBridge);
		LunchRequest request = new LunchRequest();
		request.add(new LunchOption("foo"));
		request.add(new LunchOption("baz"));
		subject.processSentRequest(request);
		
		
		String actual = skypeBridge.sentMessage;
		String expected ="Almoço!\n" +
				"1) foo\n" +
				"2) baz";
		assertEquals(expected, actual);
		
		String actualChatId = skypeBridge.toChatId;
		String expectedId = "autoid";
		assertEquals(expectedId, actualChatId);
	}
}
