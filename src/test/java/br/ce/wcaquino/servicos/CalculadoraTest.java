package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.entidades.Calculadora;
import br.ce.wcaquino.utils.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calculadora;
	
	@Mock
	private Calculadora calcMock;
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		calculadora = new Calculadora();
	}
	
	@Test
	public void diferencaEntreMockSpy() {
		Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
		Mockito.when(calcMock.somar(1, 3)).thenCallRealMethod();
		
		//Má prática:
		Mockito.when(calcSpy.somar(1, 2)).thenReturn(5);
		
		//Boa prática:
		Mockito.doReturn(5).when(calcSpy).somar(1, 3);
		
		//Evitando que o Spy não execute um determinado método
		Mockito.doNothing().when(calcSpy).imprimir();
		
		System.out.println("Mock: " + calcMock.somar(1, 3));
		System.out.println("Mock: " + calcMock.somar(1, 2));
		System.out.println("Spy: " + calcSpy.somar(1, 3));
		
		calcMock.imprimir();
		calcSpy.imprimir();
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
