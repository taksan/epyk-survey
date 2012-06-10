package utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.UnhandledException;
import org.reflections.Reflections;


public class ReflectionUtils {

	public static List<String> getChildren(String packageClass, Class<?> father) {
	    final Reflections reflections = new Reflections(
	            new Object[] { packageClass });
	    Set<? extends Class<?>> subTypesOf = reflections.getSubTypesOf(father);
	    List<String> classNameList = ReflectionUtils.getClassNameList(subTypesOf);
	    Collections.sort(classNameList);
	    return classNameList;
	}

	public static List<String> getClassNameList(
	        Set<? extends Class<?>> subTypes) {
	    List<String> listToReturn = new ArrayList<String>();
	    for (final Class<?> aClass : subTypes) {
	        listToReturn.add(aClass.getName());
	    }
	    return listToReturn;
	}

	public static <T> T[] getInstancesOfClassesInGivenPackage(Class<T> targetInterface, String fromPackage) {
		return getInstancesOfClassesInGivenPackage(targetInterface, fromPackage,new ArgumentLessInstanceBuilder<T>());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] getInstancesOfClassesInGivenPackage(
			Class<T> targetInterface, 
			String fromPackage,
			InstanceBuilder<T> instanceBuilder) {
		List<T> factories  = new ArrayList<T>();
		List<String> children = getChildren(fromPackage, targetInterface);
		for (final String className : children) {
	        try {
	            final Class<?> actionClass = Class.forName(className);
				final T actionInstance = (T) instanceBuilder.make(actionClass);
	            factories.add(actionInstance);
	        }
	        catch(final NoSuchMethodException e) {
	        	throw new IllegalStateException(className+" is missing default constructor", e.getCause());
	        }
	        catch (final Exception e) {
	            throw new UnhandledException(e);
	        }
	    }
		
		return factories.toArray((T[]) Array.newInstance(targetInterface, 0));
	}

}
