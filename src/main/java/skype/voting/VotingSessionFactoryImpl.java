package skype.voting;

public class VotingSessionFactoryImpl implements VotingSessionFactory {

	@Override
	public VotingSession produce() {
		return new VotingSessionImpl();
	}

}
