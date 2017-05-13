package io.github.alivety.dynjava;

import java.io.IOException;

public class DynJava {
	public static void main(String[]args) throws DynException, IOException {
		DynEnviroment env=DynEnviroment.getEnviroment();
		env.interpret(System.in);
	}
}
