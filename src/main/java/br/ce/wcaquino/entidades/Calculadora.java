package br.ce.wcaquino.entidades;

import br.ce.wcaquino.utils.NaoPodeDividirPorZeroException;

public class Calculadora {
	
	public int somar(int a, int b) {
		return a+b;
	}

	public int subtrair(int a, int b) {
		return a-b;
	}

	public int multiplicar(int a, int b) {
		return a*b;
	}

	public int divisao(int a, int b) throws NaoPodeDividirPorZeroException {
		
		if(a == 0 | b == 0) {
			throw new NaoPodeDividirPorZeroException();
		}
		return a/b;
	}
	
	public void imprimir() {
		System.out.println("Executando método imprimir!");
	}

}
