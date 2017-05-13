package io.github.alivety.dynjava;

import java.lang.reflect.Method;
import java.util.HashMap;

import io.github.alivety.dynjava.values.DynJavaMethod;
import io.github.alivety.dynjava.values.DynNil;
import io.github.alivety.dynjava.values.DynNumber;
import io.github.alivety.dynjava.values.DynString;

public class DynObject implements DynJavaComponent {
	private HashMap<String,Object> variables=new HashMap<>();
	
	public void declareMethod(String name,Method m,Object o) {
		variables.put(name, new DynJavaMethod(m,o));
	}
	
	public void setVar(String name,Object o) throws DynException {
		Object put=o;
		if (o instanceof String) {
			put= new DynString((String) o);
		} else if (o instanceof Number) {
			put=new DynNumber((Number) o);
		} else if (o instanceof Method) {
			throw new DynException("Use DynObject.declareMethod");
		} else if (o==null) {
			put=new DynNil();
		}
		if (variables.containsKey(name)) {
			if (variables.get(name.toLowerCase()) instanceof DynValue) {
				if (put instanceof DynValue) {
					((DynValue)variables.get(name)).update(((DynValue)put).evaluate());
					return;
				}
			}
		}
		variables.put(name, put);
	}
	
	public Object getVar(String name) throws DynException {
		if (!variables.containsKey(name.toLowerCase())) {
			throw new DynException("There is no variable "+name);
		}
		Object o = variables.get(name.toLowerCase());
		if (o instanceof DynObject) return (DynObject) o;
		if (o instanceof DynValue) return ((DynValue)o).evaluate();
		return new DynJavaObject(o);
	}
	
	protected Object getRefVar(String name) throws DynException {
		if (!variables.containsKey(name.toLowerCase())) {
			throw new DynException("There is no variable "+name);
		}
		return variables.get(name.toLowerCase());
	}
	
	public boolean isDeclared(String name) {
		return variables.containsKey(name.toLowerCase());
	}
	
	public String toString() {
		return this.getClass().getSimpleName()+".dumpVars: "+variables.toString();
	}
	
	public static abstract class DynValue implements DynJavaComponent {
		public abstract Object evaluate() throws DynException;
		public abstract void update(Object o) throws DynException;
	}
	
	public void dumpVars() {
		System.out.println(variables);
	}
}
