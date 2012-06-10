package utils;

import java.lang.reflect.InvocationTargetException;

public interface InstanceBuilder<T> {

	T make(Class<?> actionClass) throws 
									NoSuchMethodException, 
									IllegalArgumentException, 
									InstantiationException, 
									IllegalAccessException, 
									InvocationTargetException;
}
