package io.github.alivety.dynjava;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DynJavaObject extends DynObject {
	/**
	 * Transmutes any Java object into a DynObject.
	 * Any updates to the Java object will not be reflected.
	 * @param o
	 * @throws DynException 
	 */
	public DynJavaObject(Object reflect) throws DynException {
		if (reflect instanceof DynObject) {
			throw new DynException("Cannot transmute DynObject");
		}
		
		Class<?> o=reflect.getClass();
		try {
			handleFields(reflect,o);
			handleMethods(reflect,o);
			boolean obj=false;
			while (o.getSuperclass()!=null) {
				if (obj) break;
				if (o.getSuperclass() instanceof Object) obj=true;
				reflect=reflect.getClass().getSuperclass().cast(reflect);
				o=reflect.getClass();
				handleFields(reflect,o);
				handleMethods(reflect,o);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new DynException(e);
		}
	}
	
	private void handleFields(Object reflect,Class<?> o) throws IllegalArgumentException, IllegalAccessException, DynException {
		Field[] fields=o.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			this.setVar(f.getName(), f.get(reflect));
		}
	}
	
	private void handleMethods(Object reflect,Class<?>o) {
		Method[] methods=o.getDeclaredMethods();
		for (Method m:methods) {
			System.out.println(m);
			m.setAccessible(true);
			this.declareMethod(m.getName(), m, reflect);
		}
	}
}
