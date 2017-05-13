package io.github.alivety.dynjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

public class DynEnviroment implements DynJavaComponent {
	private DynObject env=new DynObject();
	
	public void inputVar(String name,Object o) throws DynException {
		env.setVar(name, o);
	}
	
	public Object getVar(String name) throws DynException {
		return env.getVar(name);
	}
	
	String[] keywords=new String[]{"var","raw","refvar","print"};
	String keyword="read";
	
	
	public void interpret(InputStream is) throws IOException, DynException {
		//remember to lowercase all input
		String line;
		StringBuilder master=new StringBuilder();
		BufferedReader in=new BufferedReader(new InputStreamReader(is));
		while ((line=in.readLine().toLowerCase())!=null) {
			if (line.equals("^c")) break;
			master.append(line);
		}
	}
	
	private static boolean isNumeric(String str) {
	  return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	private Object determineType(String raw) throws DynException {
		if (isNumeric(raw)) {//number
			return new BigDecimal(raw);
		} else if (raw.startsWith("\"") && raw.endsWith("\"")) {//string
			return raw.substring(1, raw.length()-1);
		} else if (raw.equals("nil")) {
			return null;
		}
		else {//pass by value variable assignment
			return env.getVar(raw);
		}
	}
	
	/*private void interpretRefVar(StringBuilder buffer) throws DynException {
		String rel=buffer.toString();
		buffer.setLength(0);
		String var=null,val=null;
		for (char c : rel.toCharArray()) {
			if (c=='=') {
				var=buffer.toString();
				buffer.setLength(0);
				continue;
			}
			buffer.append(c);
		}
		val=buffer.toString();
		buffer.setLength(0);
		keyword="read";
		
		if (!env.isDeclared(val)) {
			throw new DynException("No variable "+val+" to assign reference to");
		} else {
			env.setVar(var, env.getRefVar(val));
		}
	}
	
	private void interpretRaw(StringBuilder buffer) throws DynException {
		String rel=buffer.toString();
		buffer.setLength(0);
		for (char c : rel.toCharArray()) {
			buffer.append(c);
		}
		String var=buffer.toString();
		buffer.setLength(0);
		keyword="read";
		
		System.out.println(env.getRefVar(var));
	}
	
	private void interpretPrint(StringBuilder buffer) throws DynException {
		String rel=buffer.toString();
		buffer.setLength(0);
		for (char c : rel.toCharArray()) {
			buffer.append(c);
		}
		String var=buffer.toString();
		buffer.setLength(0);
		keyword="read";
		
		System.out.println(env.getVar(var));
	}
	
	private void interpretVar(StringBuilder buffer) throws DynException {
		String rel=buffer.toString();
		buffer.setLength(0);
		String var=null,val=null;
		for (char c : rel.toCharArray()) {
			if (c=='=') {
				var=buffer.toString();
				buffer.setLength(0);
				continue;
			}
			buffer.append(c);
		}
		val=buffer.toString();
		buffer.setLength(0);
		keyword="read";
		
		env.setVar(var, determineType(val));
	}*/
	
	protected DynEnviroment() {}
	
	public static DynEnviroment getEnviroment() {
		DynEnviroment env=new DynEnviroment();
		
		return env;
	}
}
