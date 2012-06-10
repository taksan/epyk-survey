package skype.shell;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import utils.ReflectionUtils;

public class CommandProcessorTest { 
	
	@Test
	public void ensureCommandProcessorHandlesAllRequests() throws ClassNotFoundException{
		Class<CommandProcessor> subject = CommandProcessor.class;
		Method[] methods = subject.getMethods();
		
		List<String> supportedRequest = new ArrayList<String>();
		for (Method method : methods) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			for (Class<?> requestsSupportedByProcessor : parameterTypes) {
				if (ShellCommand.class.isAssignableFrom(requestsSupportedByProcessor))
					supportedRequest.add(requestsSupportedByProcessor.getName());
			}
		}
		
		List<String> requests = ReflectionUtils.getChildren(
				"skype.voting.requests",
				AbstractShellCommand.class);
		
		StringBuilder unsupportedRequests = new StringBuilder();
		for (String requestName : requests) {
			if (supportedRequest.contains(requestName))
				continue;
			unsupportedRequests.append(requestName+"\n"); 
		}
		if (unsupportedRequests.toString().length()>0) {
			throw new IllegalStateException(
					"Processor has no method to handle the following requests:\n " + 
					unsupportedRequests.toString()+
					"The supported ones are:\n"+
					supportedRequest.toString());
		}
	}
}

