package skype.voting.processors;

import java.util.Set;

import skype.shell.ShellCommand;
import skype.voting.VoteOptionAndCount;
import skype.voting.VotingPollCommandExecutor;
import skype.voting.VotingPollOption;
import skype.voting.VotingSession;
import skype.voting.WinnerConsultant;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.ClosePollRequest;

public class ClosePollProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof ClosePollRequest;
	}

	@Override
	public void process(final ShellCommand command) {
		if (!executor.isInitializedSessionOnRequestChat(command)) return;
		
		final ClosePollRequest request = (ClosePollRequest) command;
		
		final VotingSession votingSession = executor.getSessionForRequest(command);
		
		votingSession.acceptWinnerConsultant(new WinnerConsultant() {
			@Override
			public void onWinner(VoteOptionAndCount winnerStats) {
				String voteStatus = VotingPollCommandExecutor.getVotingStatusMessage(votingSession);
				String winnerMessage = 
						String.format("WINNER: ***%s*** with %d vote%s",
								winnerStats.optionName,
								winnerStats.voteCount,
								getPlural(winnerStats.voteCount)
								);
				String reply = "Votes: " + voteStatus + "\n" +	winnerMessage;
				executor.removeSessionForGivenRequest(request);
				onReply(command, reply);
			}

			@Override
			public void onTie(Set<VotingPollOption> tiedOptions, int tieCount) {
				String voteStatus = VotingPollCommandExecutor.getVotingStatusMessage(votingSession);
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
				
				executor.removeSessionForGivenRequest(request);
				onReply(command, reply);
			}
			
			private String getPlural(Integer voteCount) {
				String string = voteCount>1?"s":"";
				return string;
			}
		});
	}
}
