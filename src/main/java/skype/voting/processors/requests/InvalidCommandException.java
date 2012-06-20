package skype.voting.processors.requests;

@SuppressWarnings("serial")
public class InvalidCommandException extends RuntimeException {

	public InvalidCommandException(String message) {
		super("Invalid command "+ message);
	}

}
