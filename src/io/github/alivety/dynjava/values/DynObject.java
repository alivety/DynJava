package io.github.alivety.dynjava.values;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynValue;

/**
 * A DynObject is a {@link DynValue} which contains other DynValues
 * @author dyslabs
 *
 */
public class DynObject extends DynValue {
	private HashMap<String,DynValue> vars=new HashMap<>(); 
	
	public void setVar(String name,DynValue v) throws DynException {
		if (v==null) throw new DynException("Use DynNil to pass null");
		if (isDeclared(name)) {
			if (v.getType().equals(this.getType())) {
				getVar(name).update(v);
				return;
			}
		}
		vars.put(name.toLowerCase(), v);
	}
	
	
	
	public boolean isDeclared(String name) {
		return vars.containsKey(name.toLowerCase());
	}
	
	public DynValue getVar(String name) throws DynException {
		if (!isDeclared(name)) throw new DynException("No reference to var:"+name+" exists");
		return vars.get(name.toLowerCase());
	}



	@Override
	public String getType() {
		return "object";
	}

	@Override
	public void update(DynValue n) throws DynException {
		if (!(n instanceof DynObject)) throw new DynException("Passed reference is not a "+getType());
		this.vars=((DynObject)n).vars;
	}
	
	public String toString() {
		return super.toString()+dumpVars();
	}
	
	public String dumpVars() {
		return vars.toString();
	}
}
