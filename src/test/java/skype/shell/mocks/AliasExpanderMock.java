package skype.shell.mocks;

import java.util.Map;

import skype.shell.AliasExpander;
import skype.shell.Persistence;
import skype.shell.PersistenceMock;
import skype.shell.RemoveAliasFeedbackHandler;

public class AliasExpanderMock implements AliasExpander {

	private final Persistence persistence2;

	public AliasExpanderMock(Persistence persistence) {
		persistence2 = persistence;
	}

	public AliasExpanderMock() {
		persistence2 = new PersistenceMock();
	}

	private boolean expandWasInvoked;
	private boolean createNewAliasInvoked;
	private boolean getAliasesInvoked;
	private boolean removeAliasInvoked;
	private boolean shouldFailOnRemoval = false;
	private String expandedMessage;
	
	public boolean expandWasInvoked() {
		return expandWasInvoked;
	}

	@Override
	public String expand(String message) {
		expandWasInvoked = true;
		return expandedMessage;
	}
	
	@Override
	public void createNewAlias(String alias, String expansion){
		createNewAliasInvoked = true;
	}
	
	@Override
	public Map<String, String> getAliases() {
		getAliasesInvoked = true;
		return persistence2.loadAliases();
	}
	
	@Override
	public void removeAlias(String alias, RemoveAliasFeedbackHandler feedback){
		removeAliasInvoked = true;
		if (shouldFailOnRemoval)
			feedback.onAliasDoesNotExist();
		else
			feedback.onSuccess();
	}

	public void setFailOnRemoval() {
		shouldFailOnRemoval = true;
	}
	
	public boolean createNewAliasInvoked() {
		return createNewAliasInvoked;
	}

	public boolean getAliasesInvoked() {
		return getAliasesInvoked;
	}

	public boolean removeAliasInvoked() {
		return removeAliasInvoked;
	}

	public void setExpandedMessage(String expanded) {
		this.expandedMessage = expanded;
	}


}
