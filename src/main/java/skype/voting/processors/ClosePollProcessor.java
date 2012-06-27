package skype.voting.processors;

import java.util.Set;

import skype.shell.ShellCommand;
import skype.voting.application.VoteOptionAndCount;
import skype.voting.application.VotingPollOption;
import skype.voting.application.VotingSession;
import skype.voting.application.WinnerConsultant;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.ClosePollInterpreter;
import skype.voting.processors.requests.ClosePollRequest;

public class ClosePollProcessor extends VotingCommandProcessorAbstract {
	
	public ClosePollProcessor() {
		super(new ClosePollInterpreter());
	}

	@Override
	public void process(final ShellCommand command) {
		if (!votingModel.isInitializedSessionOnRequestChat(command)) return;
		
		final ClosePollRequest request = (ClosePollRequest) command;
		
		final VotingSession votingSession = votingModel.getSessionForRequest(command);
		
		votingSession.acceptWinnerConsultant(new WinnerConsultant() {
			@Override
			public void onWinner(VoteOptionAndCount winnerStats) {
				String voteStatus = messages.getVotingStatusMessage(votingSession);
				String winnerMessage = 
						String.format("WINNER: ***%s*** with %d vote%s",
								winnerStats.optionName,
								winnerStats.voteCount,
								getPlural(winnerStats.voteCount)
								);
				String reply = "Votes: " + voteStatus + "\n" +	winnerMessage;
				votingModel.removeSessionForGivenRequest(request);
				onReply(command, reply);
			}

			@Override
			public void onTie(Set<VotingPollOption> tiedOptions, int tieCount) {
				String voteStatus = messages.getVotingStatusMessage(votingSession);
				StringBuilder winnerMessage = new StringBuilder();
				for (VotingPollOption option : tiedOptions) {
					winnerMessage.append(option.getName()+" and ");
				}
				
				String tiedOptionNames = winnerMessage.toString().replaceFirst(" and $", "");
				String reply = "Votes: " + voteStatus + "\n" +
						String.format("TIE: **%s** tied with %s vote%s",
								tiedOptionNames,
								tieCount,
								getPlural(tieCount)
								);
				
				votingModel.removeSessionForGivenRequest(request);
				onReply(command, reply);
			}
			
			private String getPlural(Integer voteCount) {
				String string = voteCount!=1?"s":"";
				return string;
			}
		});
	}
}
