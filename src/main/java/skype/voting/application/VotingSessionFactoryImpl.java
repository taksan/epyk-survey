package skype.voting.application;


public class VotingSessionFactoryImpl implements VotingSessionFactory {

	@Override
	public VotingSession produce() {
		return new VotingSessionImpl();
	}

}
