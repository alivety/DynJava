package io.github.alivety.dynjava.values;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynObject;

public class DynString extends DynObject.DynValue {
	String v;
	public DynString(String v) {
		this.v=v;
	}
	
	@Override
	public Object evaluate() {
		return v;
	}

	@Override
	public void update(Object o) throws DynException {
		if (o==null) throw new DynException("Cannot update to null");
		v=o.toString();
	}
}
