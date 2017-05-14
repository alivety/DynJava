package io.github.alivety.dynjava.values;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynValue;

public class DynNumber extends DynEvaluatableValue<Number> {
	private Number n;
	public DynNumber(Number n) {
		this.n=n;
	}
	
	@Override
	public Number evaluate(Object... args) {
		return n;
	}

	@Override
	public String getType() {
		return "number";
	}

	@Override
	public void update(DynValue n) throws DynException {
		if (!(n instanceof DynString)) throw new DynException("Passed reference is not "+getType());
		this.n=((DynNumber)n).n;
	}

}
