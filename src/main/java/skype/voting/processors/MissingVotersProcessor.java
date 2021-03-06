package skype.voting.processors;

import org.apache.commons.lang.StringUtils;

import skype.shell.ShellCommand;
import skype.voting.application.ParticipantConsultant;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.MissingVotersInterpreter;

public class MissingVotersProcessor extends VotingCommandProcessorAbstract {
	
	public MissingVotersProcessor() {
		super(new MissingVotersInterpreter());
	}

	@Override
	public void process(ShellCommand command) {
		if (!votingModel.isInitializedSessionOnRequestChat(command)) return;
		
		VotingSession votingSession = votingModel.getSessionForRequest(command);
		
		final StringBuilder sb = new StringBuilder();
		votingSession.acceptParticipantConsultant(new ParticipantConsultant() {

			@Override
			public void visit(String participantName, boolean hasVoted) {
				if (!hasVoted)
					sb.append(participantName+", ");
			}
		});
		if (sb.toString().isEmpty()) {
			onReply(command, "Everyone already voted.");
			return;
		}
		String withoutTrailingCommand = StringUtils.substring(sb.toString(),0,-2);
		String reply = "Users that haven't voted yet:\n\t"+withoutTrailingCommand;
		onReply(command, reply);
	}

}
