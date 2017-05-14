package io.github.alivety.dynjava;

public class TokenAcceptor {
	private StringBuilder buffer=new StringBuilder();
	public void accept(char c) {
		buffer.append(c);
	}
	
	public void accept(char[]cs) {
		for (char c:cs) accept(c);
	}
	
	public String consume() {
		String res=buffer.toString();
		buffer.setLength(0);
		return res;
	}
	
	public String value() {
		return buffer.toString();
	}
}
