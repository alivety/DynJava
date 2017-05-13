package io.github.alivety.dynjava.values;
import io.github.alivety.dynjava.DynObject;

public class DynNumber implements DynObject.DynValue {
	private Number num;
	
	public DynNumber(Number num) {
		this.num=num;
	}
	
	@Override
	public Object evaluate() {
		return num;
	}

}
