package io.github.alivety.dynjava;

import java.lang.reflect.InvocationTargetException;

/**
 * A DynValue represents any data structure: objects, numbers, strings...
 * @author dyslabs
 *
 */
public abstract class DynValue {
	/**
	 * Returns the name of this dynamic type
	 * @return
	 */
	public abstract String getType();
	/**
	 * Update this dynamic value to match the passed value
	 * @param n
	 */
	public abstract void update(DynValue n) throws DynException;
	
	public String toString() {
		return this.getClass().getSimpleName()+":"+getType();
	}
	
	public String getValue() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DynException {
		return toString();
	}
}
