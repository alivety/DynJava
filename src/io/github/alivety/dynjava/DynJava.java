package io.github.alivety.dynjava;

public class DynJava {
	private String hi="hi u ok";
	private int i=5;
	private byte b=1;
	private short s=6;
	private float f=99;
	private double d=55.55;
	private long l=6L;
	public static void main(String[]args) throws DynException {
		DynJava j=new DynJava();
		DynObject o=new DynJavaObject(j);
		System.out.println(o.getVar("hi"));
		System.out.println(o.getVar("l"));
	}
}
