package skype.voting.requests.factories;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.UnhandledException;
import org.reflections.Reflections;

import skype.shell.ShellCommandFactory;

public class VotingFactoriesRetriever {
	private static final String SKYPE_VOTING_REQUESTS_PACKAGE = VotingFactoriesRetriever.class.getPackage().getName();

	public static ShellCommandFactory[] getFactories() {
		List<ShellCommandFactory> factories  = new ArrayList<ShellCommandFactory>();
		List<String> children = getChildren(SKYPE_VOTING_REQUESTS_PACKAGE, ShellCommandFactory.class);
		for (final String factoryClassName : children) {
            try {
                final Class<?> actionClass = Class.forName(factoryClassName);
                final Constructor<?> constructor = actionClass.getConstructor();
                final ShellCommandFactory actionInstance = (ShellCommandFactory) constructor.newInstance();
                factories.add(actionInstance);
            }
            catch(final NoSuchMethodException e) {
            	throw new IllegalStateException(factoryClassName+" is missing constructor without arguments", e.getCause());
            }
            catch (final Exception e) {
                throw new UnhandledException(e);
            }
        }

		return factories.toArray(new ShellCommandFactory[0]);
	}
	
    private static List<String> getChildren(String packageClass, Class<?> father) {
        final Reflections reflections = new Reflections(
                new Object[] { packageClass });
        Set<? extends Class<?>> subTypesOf = reflections.getSubTypesOf(father);
        List<String> classNameList = getClassNameList(subTypesOf);
        Collections.sort(classNameList);
        return classNameList;
    }
    
    private static List<String> getClassNameList(
            Set<? extends Class<?>> subTypes) {
        List<String> listToReturn = new ArrayList<String>();
        for (final Class<?> aClass : subTypes) {
            listToReturn.add(aClass.getName());
        }
        return listToReturn;
    }
}