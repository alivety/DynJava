package io.github.alivety.dynjava.values;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynObject;

public class DynNil extends DynObject.DynValue {
	@Override
	public Object evaluate() throws DynException {
		return null;
	}

	@Override
	public void update(Object o) throws DynException {}
}
