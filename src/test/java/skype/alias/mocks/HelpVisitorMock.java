package skype.alias.mocks;

import skype.voting.HelpVisitor;

public class HelpVisitorMock implements HelpVisitor {

	private final StringBuilder operations;

	public HelpVisitorMock(StringBuilder operations) {
		this.operations = operations;
	}

	@Override
	public HelpVisitor onTopLevel(String topicMessage) {
		operations.append("onTopLevel: " + topicMessage + "\n");
		return this;
	}

	@Override
	public HelpVisitor onTopic(String subtopic) {
		operations.append("onTopic: " + subtopic + "\n");
		return this;
	}

	@Override
	public HelpVisitor onTopicDescription(String topicDescription) {
		operations.append("onTopicDescription: " + topicDescription + "\n");
		return this;
	}

}
