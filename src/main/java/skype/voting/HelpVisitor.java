package skype.voting;

public interface HelpVisitor {
	public HelpVisitor onTopLevel(String topicMessage);
	public HelpVisitor onTopic(String subtopic);
	public HelpVisitor onTopicDescription(String topicDescription);
}
