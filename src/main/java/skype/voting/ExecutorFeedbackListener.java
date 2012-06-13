package skype.voting;

public interface ExecutorFeedbackListener {
	public void onSuccess(String replyMessage);
	public void onSuccessPublicReply(String replyMessage);
}
