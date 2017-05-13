package io.github.alivety.dynjava;

import java.lang.reflect.Method;
import java.util.HashMap;

import io.github.alivety.dynjava.values.DynJavaMethod;
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
		}
		variables.put(name, put);
	}
	
	public Object getVar(String name) throws DynException {
		if (!variables.containsKey(name)) {
			throw new DynException("There is no variable "+name);
		}
		Object o = variables.get(name);
		if (o instanceof DynObject) return (DynObject) o;
		if (o instanceof DynValue) return ((DynValue)o).evaluate();
		return new DynJavaObject(o);
	}
	
	public String toString() {
		return variables.toString();
	}
	
	public interface DynValue extends DynJavaComponent {
		public Object evaluate() throws DynException;
	}
}
