package io.github.alivety.dynjava.values;

public abstract class DynMethod extends DynEvaluatableValue<Object> {
	@Override
	public String getType() {
		return "method";
	}
}
