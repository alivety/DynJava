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
	
	String[] keywords=new String[]{"var","raw"};
	String keyword="read";
	
	public void interpret(InputStream is) throws IOException, DynException {
		try {
		BufferedReader in=new BufferedReader(new InputStreamReader(is));
		StringBuilder buf=new StringBuilder();
		String line;
		while ((line=in.readLine())!=null) {
			for (char c : line.toCharArray()) {
				if (c==' ') continue;
				buf.append(c);
				if (Arrays.asList(keywords).contains(buf.toString()) ) {
					if (!keyword.equals("read")) throw new DynException("Malformed DynJava: current executing "+keyword+", received new command "+buf.toString());
					keyword=buf.toString();
					buf.setLength(0);//consume token
				} 
			}
			
			if (keyword.equals("var")) {
				interpretVar(buf);
			} else if (keyword.equals("refvar")) {
				interpretRefVar(buf);
			} else if (keyword.equals("raw")) {
				interpretRaw(buf);
			}
			
			env.dumpVars();
		}
		} catch (IOException e) {
			throw e;
		} catch (DynException e) {
			throw e;
		}
		catch (Exception e) {
			throw new DynException(e);
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
		} else {//pass by value variable assignment
			return env.getVar(raw);
		}
	}
	
	private void interpretRefVar(StringBuilder buffer) throws DynException {
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
	}
	
	protected DynEnviroment() {}
	
	public static DynEnviroment getEnviroment() {
		DynEnviroment env=new DynEnviroment();
		
		return env;
	}
}
