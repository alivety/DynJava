package io.github.alivety.dynjava;

import java.util.HashMap;

public class DynEnviroment implements DynJavaComponent {
	private HashMap<String,DynObject> vars=new HashMap<>();
	
	private void inputGlobal(String name,DynObject var) {
		vars.put(name, var);
	}
	
	public void inputVar(String name,Object o) {
		if (o instanceof DynObject) {
			inputGlobal(name,(DynObject) o);
		} else {
			
		}
	}
	
	protected DynEnviroment() {}
}
