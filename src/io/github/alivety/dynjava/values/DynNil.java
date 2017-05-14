package io.github.alivety.dynjava.values;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynValue;

public class DynNil extends DynValue {
	@Override
	public String getType() {
		return "nil";
	}

	@Override
	public void update(DynValue n) throws DynException {}
}
