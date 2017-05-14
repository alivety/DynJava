package io.github.alivety.dynjava.values;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynValue;

public class DynArray extends DynEvaluatableValue<Object[]>{
	private ArrayList<Object> list=new ArrayList<>();
	
	public DynArray(Object[] o) {
		list.addAll(Arrays.asList(o));
	}
	
	@Override
	public Object[] evaluate(Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DynException {
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public String getType() {
		return "array";
	}

	@Override
	public void update(DynValue n) throws DynException {
		if (!(n instanceof DynArray)) throw new DynException("Passed reference is not "+getType());
		this.list=((DynArray)n).list;
	}

}
