package io.github.alivety.dynjava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import io.github.alivety.dynjava.values.DynJavaMethod;
import io.github.alivety.dynjava.values.DynNil;
import io.github.alivety.dynjava.values.DynNumber;
import io.github.alivety.dynjava.values.DynObject;
import io.github.alivety.dynjava.values.DynString;

/**
 * Helper class for transforming Java classes/constructs to DynJava values
 * @author dyslabs
 *
 */
public class DynJava {
	/**
	 * Converts any object to an equaliavent DynObject. All variables and methods will be copied via reflection.<br/>
	 * It will check automatically for strings and numbers and convert them accordingly.
	 * @param o
	 * @return
	 * @throws DynException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static DynValue convert(Object o) throws DynException, IllegalArgumentException, IllegalAccessException {
		if (o instanceof DynValue) throw new DynException("Cannot transmute DynValue as a DynValue");
		if (o==null) return new DynNil();
		if (o instanceof String) return new DynString((String)o);
		if (o instanceof Number) return new DynNumber((Number)o);
		if (o instanceof Method) throw new DynException("Cannot transmute native method without reference, use DynJava.convert(Object,Method)");
		
		DynObject res=new DynObject();
		Class<?> c=o.getClass();
		boolean obj=false;
		handleFields(res,o,c);
		handleMethods(res,o,c);
		while (c.getSuperclass()!=null) {
			if (obj) break;
			if (c.getSuperclass() instanceof Object) obj=true;
			c=c.getSuperclass();
			handleFields(res,o,c);
			handleMethods(res,o,c);
		}
		
		return res;
	}
	
	public static DynJavaMethod convert(Object ref,Method m) {
		return new DynJavaMethod(ref,m);
	}
	
	private static void handleFields(DynObject res,Object reflect,Class<?> o) throws IllegalArgumentException, IllegalAccessException, DynException {
		Field[] fields=o.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			res.setVar(f.getName(), convert(f.get(reflect)));
		}
	}
	
	private static void handleMethods(DynObject res,Object reflect,Class<?>o) throws DynException, IllegalArgumentException, IllegalAccessException {
		Method[] methods=o.getDeclaredMethods();
		for (Method m:methods) {
			if (res.isDeclared(m.getName().toLowerCase())) continue;
			m.setAccessible(true);
			res.setVar(m.getName().toLowerCase(), convert(reflect,m));
		}
	}
	
	private void eatMe() {
		System.out.println("please dont");
	}
	
	public static void main(String[]args) throws IllegalArgumentException, IllegalAccessException, DynException, InvocationTargetException, FileNotFoundException, IOException {
		System.out.println(new BigDecimal(0).add(new BigDecimal(8)));
		DynObject o=(DynObject) DynJava.convert(new DynJava());
		o.setVar("array", DynJava.convert(new String[]{"hi","u","ok"}));
		System.out.println(o);
		System.out.println(((DynJavaMethod)o.getVar("eatme")).evaluate());//methods are variables !!
		System.out.println();
		DynEnviroment env=DynEnviroment.getEnviroment();
		env.interpret(new FileInputStream("example.dava"), null);
		System.out.println("x="+env.getJavaVar("x"));
		System.out.println("y="+env.getJavaVar("y"));
		System.out.println("z="+env.getJavaVar("z"));
		System.out.println("0u="+env.getJavaVar("0u"));
		System.out.println("m="+env.getJavaVar("m"));
	}
}
