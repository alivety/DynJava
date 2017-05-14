package io.github.alivety.dynjava;

import io.github.alivety.dynjava.values.DynJavaMethod;
import io.github.alivety.dynjava.values.DynObject;

public class DynEnviroment {
	private DynEnviroment() {}
	private DynObject env=new DynObject();
	
	public void setVar(String name,DynValue v) throws DynException {
		env.setVar(name, v);
	}
	
	public DynValue getVar(String name) throws DynException {
		return env.getVar(name);
	}
	
	public static DynEnviroment getEnviroment() throws DynException {
		try {
			DynEnviroment env=new DynEnviroment();
			env.setVar("print", new DynJavaMethod(System.out,System.out.getClass().getMethod("println", Object.class)));
			env.setVar("enviroment", env.env);
			return env;
		} catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
			throw new DynException(e);
		}
	}
	
	public String dumpVars() {
		return env.dumpVars();
	}
	
	public String toString() {
		return "DynEviroment:enviroment";
	}
}
