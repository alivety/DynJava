package io.github.alivety.dynjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import io.github.alivety.dynjava.values.DynEvaluatableValue;
import io.github.alivety.dynjava.values.DynJavaMethod;
import io.github.alivety.dynjava.values.DynNumber;
import io.github.alivety.dynjava.values.DynObject;
import io.github.alivety.dynjava.values.DynString;

public class DynEnviroment {
	private DynEnviroment() {}
	private DynObject env=new DynObject();
	
	public void setVar(String name,DynValue v) throws DynException {
		env.setVar(name, v);
	}
	
	public DynValue getVar(String name) throws DynException {
		return env.getVar(name);
	}
	
	public Object getJavaVar(String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DynException {
		DynValue v=this.getVar(name);
		if (v instanceof DynEvaluatableValue) {
			return ((DynEvaluatableValue<?>)v).evaluate();
		} else {
			return v;
		}
	}
	
	public static DynEnviroment getEnviroment() throws DynException {
		try {
			DynEnviroment env=new DynEnviroment();
			env.setVar("print", new DynJavaMethod(System.out,System.out.getClass().getMethod("println", Object.class)));
			env.setVar("enviroment", env.env);
			return env;
		} catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
			throw new DynException(e);
		}
	}
	
	public String dumpVars() {
		return env.dumpVars();
	}
	
	public String toString() {
		return "DynEviroment:enviroment";
	}
	
	private String[] operations=new String[]{"var"};
	
	public void interpret(InputStream is,String end) throws IOException, DynException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		BufferedReader in=new BufferedReader(new InputStreamReader(is));
		/**
		 * Abstract overview:
		 * Read input token by token
		 * Create abstract syntax tree
		 * Read/execute syntax tree
		 */
		String line;
		StringBuilder master=new StringBuilder();
		int lnc=1;
		while ((line=in.readLine())!=null) {
			if (line.contains(";")) {
				throw new DynException("Error on line "+lnc+" at character index "+line.indexOf(";")+": Semicolons are not permitted");
			}
			master.append(line).append(";");
			lnc++;
		}
		ArrayList<CodeBlock> syntaxTree=new ArrayList<>();
		String[] statements=master.toString().split(";");
		for (int j=0;j<statements.length;j++) {
			String s=statements[j];
			String op="read";
			TokenAcceptor buffer=new TokenAcceptor();
			for (int i=0;i<s.length();i++) {
				char c=s.charAt(i);
				//if (c==' ' && op.equals("read")) continue;
				buffer.accept(c);
				if (Arrays.asList(operations).contains(buffer.value())) {
					if (!op.equals("read")) {
						throw new DynException("Error on line "+j+" at character index "+i+": Received keyword "+buffer.value()+" while performing operation "+op);
					}
					op=buffer.consume();
				}
			}
			
			if (op.equals("var")) {
				String c=buffer.consume();
				String var=null,val=null;
				for (char x : c.toCharArray()) {
					if (x==' ' && var==null) continue;
					if (x=='=') {
						var=buffer.consume();
						continue;
					}
					buffer.accept(x);
				}
				val=buffer.consume();
				syntaxTree.add(new CodeBlock("def",var,val));
			}
		}
		
		System.out.println("syntax tree = "+syntaxTree);
		Iterator<CodeBlock> treeWalker=syntaxTree.iterator();
		while (treeWalker.hasNext()) {
			CodeBlock cb=treeWalker.next();
			String cmd=cb.cmd;
			String[] args=cb.args;
			switch (cmd) {
			case "def":
				env.setVar(interpret(args[0]), convert(interpret(args[1])));
				break;
			}
		}
	}
	
	public String interpret(String arg) throws DynException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String iarg=arg;
		TokenAcceptor buffer=new TokenAcceptor();
		for (char c:arg.toCharArray()) {
			buffer.accept(c);
			if (buffer.value().matches("\\*[a-zA-z0-0]")) {
				String r=buffer.consume();
				arg=arg.replace(r, env.getVar(r.replace("*", "")).getValue());
			}
		}
		buffer.consume();
		if (arg.matches(".*[0-9]")) {
			String op="";
			boolean doop=false;
			BigDecimal d=new BigDecimal(0);
			for (char c:arg.toCharArray()) {
				if (isNumeric(String.valueOf(c))) {
					if (doop) {
						d=new BigDecimal(buffer.consume());
						doop=false;
					switch (op) {
					case "+":
						d=d.add(new BigDecimal(String.valueOf(c)));
						break;
					case "-":
						d=d.subtract(new BigDecimal(String.valueOf(c)));
						break;
					case "*":
						d=d.multiply(new BigDecimal(String.valueOf(c)));
						break;
					case "/":
						d=d.divide(new BigDecimal(String.valueOf(c)));
						break;
					case "%":
						d=d.divide(new BigDecimal(String.valueOf(c)));
						break;
						default:
							throw new DynException("Unrecongized math operation: "+op);
					} 
					buffer.accept(d.toPlainString().toCharArray());
					}else {
						buffer.accept(c);
					}
				} else {
					op=String.valueOf(c);
					doop=true;
				}
			}
			arg=d.toPlainString();
		}
		System.out.println("interpret("+iarg+"): "+arg); 
		return arg;
	}
	
	public DynValue convert(String o) throws IllegalArgumentException, IllegalAccessException, DynException {
		if (isNumeric(o)) return new DynNumber(new BigDecimal(o));
		if (o.startsWith("\"")&&o.endsWith("\"")) return new DynString(o.substring(1, o.length()-1));
		if (env.isDeclared(o)) return env.getVar(o);
		return DynJava.convert(o);
	}
	
	private boolean isNumeric(String str) {
		 return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	static class CodeBlock {
		private String cmd;
		private String[] args;
		public CodeBlock(String...args) {
			this.cmd=args[0];
			this.args=new String[args.length-1];
			for (int i=1;i<args.length;i++) {
				if (i>=args.length) break;
				this.args[i-1]=args[i];
			}
		}
		
		public String toString() {
			StringBuilder sb=new StringBuilder(cmd).append("(");
			for (int i=0;i<args.length;i++) {
				sb.append(args[i]);
				if (i<args.length-1) sb.append(",");
			}
			return sb.append(")").toString();
		}
	}
}
