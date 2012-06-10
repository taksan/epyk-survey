package utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ArgumentLessInstanceBuilder<T> implements InstanceBuilder<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T make(Class<?> actionClass) throws 
									NoSuchMethodException, 
									IllegalArgumentException, 
									InstantiationException, 
									IllegalAccessException, 
									InvocationTargetException 
	{
		final Constructor<?> constructor = actionClass.getConstructor();
		return (T) constructor.newInstance();
	}

}
