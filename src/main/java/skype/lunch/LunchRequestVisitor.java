package skype.lunch;

public interface LunchRequestVisitor {
	public void visit(LunchOption option);
}
