package io.github.alivety.dynjava.values;

import java.lang.reflect.InvocationTargetException;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynValue;

public abstract class DynEvaluatableValue<R> extends DynValue {
	public abstract R evaluate(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DynException;public String toString() {
		return this.getClass().getSimpleName()+":"+getType();
	}
}
