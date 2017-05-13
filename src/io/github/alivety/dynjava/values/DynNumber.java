package io.github.alivety.dynjava.values;
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

}
