package io.github.alivety.dynjava.values;

import io.github.alivety.dynjava.DynObject;

public class DynString implements DynObject.DynValue {
	final String v;
	public DynString(String v) {
		this.v=v;
	}
	
	@Override
	public Object evaluate() {
		return v;
	}
}
