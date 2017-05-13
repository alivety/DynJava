package io.github.alivety.dynjava.values;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynObject;

public class DynJavaMethod implements DynObject.DynValue {
	private Object[] args=null;
	private Method m;
	private Object o;
	
	public DynJavaMethod(Method m,Object o) {
		this.m=m;
		this.o=o;
	}
	
	@Override
	public Object evaluate() throws DynException {
		if (args==null) {
			throw new DynException("Call DynMethod.invoke");
		}
		Object res;
		try {
			res=m.invoke(o,args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			this.args=null;
			throw new DynException(e);
		}
		this.args=null;
		return res;
	}

	public Object invoke(Object[]args) throws DynException {
		this.args=args;
		return this.evaluate();
	}
}
