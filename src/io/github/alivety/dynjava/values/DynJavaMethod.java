package io.github.alivety.dynjava.values;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynJava;
import io.github.alivety.dynjava.DynValue;

/**
 * Defines a dynamic method that links to a Java method
 * @author dyslabs
 *
 */
public class DynJavaMethod extends DynMethod {
	private Object ref;
	private Method m;
	
	/**
	 * ref is the object which m is called from
	 * @param ref
	 * @param m
	 */
	public DynJavaMethod(Object ref,Method m) {
		this.ref=ref;
		this.m=m;
	}

	@Override
	public Object evaluate(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DynException {
		return DynJava.convert(m.invoke(ref, args));
	}

	@Override
	public void update(DynValue n) throws DynException {
		if (!(n instanceof DynJavaMethod)) throw new DynException("Passed reference is not "+getType());
		DynJavaMethod djm=(DynJavaMethod)n;
		this.ref=djm.ref;
		this.m=djm.m;
	}
}
