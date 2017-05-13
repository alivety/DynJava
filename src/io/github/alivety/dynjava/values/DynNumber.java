package io.github.alivety.dynjava.values;
import io.github.alivety.dynjava.DynException;
import io.github.alivety.dynjava.DynObject;

public class DynNumber extends DynObject.DynValue {
	private Number num;
	
	public DynNumber(Number num) {
		this.num=num;
	}
	
	@Override
	public Object evaluate() {
		return num;
	}

	@Override
	public void update(Object o) throws DynException {
		if (o instanceof Number) {
			num=(Number) o;
		} else {
			throw new DynException("Cannot update to non-number");
		}
	}

}
