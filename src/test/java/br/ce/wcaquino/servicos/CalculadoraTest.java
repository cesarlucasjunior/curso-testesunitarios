package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.entidades.Calculadora;
import br.ce.wcaquino.utils.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calculadora;
	
	@Before
	public void setup() {
		calculadora = new Calculadora();
	}
	
	@Test
	public void somarDoisNumeros() {
		//cenario
		int a = 5;
		int b = 3;
		
		//acao
		int resultadoSoma = calculadora.somar(a,b);
		
		//verificacao
		Assert.assertEquals(8, resultadoSoma);
	}
	
	@Test
	public void subtrairDoisNumero() {
		//cenario
		int a = 5;
		int b = 3;
		
		//acao
		int resultadoSubtracao = calculadora.subtrair(a,b);
		
		//verificacao
		Assert.assertEquals(2, resultadoSubtracao);
	}
	
	@Test
	public void multiplicarDoisNumeros() {
		//cenario
		int a = 5;
		int b = 3;
		
		//acao
		int resultadoMultiplicacao = calculadora.multiplicar(a,b);
		
		//verificacao
		Assert.assertEquals(15, resultadoMultiplicacao);
	}
	
	@Test
	public void dividirDoisNumeros() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 6;
		int b = 3;
		
		//acao
		int resultadoDivisao = calculadora.divisao(a,b);
		
		//verificacao
		Assert.assertEquals(2, resultadoDivisao);
	}
	
	@Test(expected=NaoPodeDividirPorZeroException.class)
	public void exceptionDividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 0;
		int b = 3;
		
		//acao
		calculadora.divisao(a, b);
	}

}
