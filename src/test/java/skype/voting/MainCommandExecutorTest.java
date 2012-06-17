package skype.voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.mocks.ChatBridgeMock;

public class MainCommandExecutorTest {
	StringBuilder operations = new StringBuilder();
	
	final CommandExecutor[] processorUnits = new CommandExecutor[]{
			new CommandExecutor() {
				@Override
				public boolean processMessage(ChatAdapterInterface chat, String message) {
					operations.append("#1: " + message + "\n");
					return true;
				}
				
				@Override
				public void setReplyListener(ReplyListener listener) {
					operations.append("setReplyListener #1\n");
				}
			},
			new CommandExecutor() {
				@Override
				public boolean processMessage(ChatAdapterInterface chat, String message) {
					operations.append("#2 " + message + "\n");
					return false;
				}
				@Override
				public void setReplyListener(ReplyListener listener) {
					operations.append("setReplyListener #2\n");
				}
			}
	};
	MainCommandExecutor subject = new MainCommandExecutor(processorUnits);
	
	@Test
	public void onProcessMessage_ShouldDelegateToProcessorUnitAndStopWhenItReturnsTrue(){
		subject.processMessage(new ChatBridgeMock(), "foo");
		String expected = 
				"#1: foo";
		assertEquals(expected, 
				operations.toString().trim());
	}
	
	@Test
	public void onSetReplyListener_ShouldInvokeSetReplyListenerForAllExecutors()
	{
		subject.setReplyListener(null);
		
		String expected=
			"setReplyListener #1\n" +
			"setReplyListener #2";
		assertEquals(expected, 
				operations.toString().trim());
	}
	
}