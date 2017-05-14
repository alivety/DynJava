package io.github.alivety.dynjava.values;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynValue;

public class DynString extends DynEvaluatableValue<String> {
	private String s;
	public DynString(String s) {
		this.s=s;
	}
	
	@Override
	public String evaluate(Object... args) {
		return s;
	}

	@Override
	public String getType() {
		return "string";
	}

	@Override
	public void update(DynValue n) throws DynException {
		if (!(n instanceof DynString)) throw new DynException("Passed reference is not "+getType());
		this.s=((DynString)n).s;
	}

}
