package io.github.alivety.dynjava;

public class DynException extends Exception {
	public DynException(String string) {
		super(string);
	}

	public DynException(Exception e) {
		super(e);
	}
}
