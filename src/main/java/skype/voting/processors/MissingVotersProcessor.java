package skype.voting.processors;

import org.apache.commons.lang.StringUtils;

import skype.shell.ShellCommand;
import skype.voting.ParticipantConsultant;
import skype.voting.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.MissingVotersRequest;

public class MissingVotersProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof MissingVotersRequest;
	}

	@Override
	public void process(ShellCommand command) {
		VotingSession votingSession = manager.getSessionForRequest(command);
		
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
