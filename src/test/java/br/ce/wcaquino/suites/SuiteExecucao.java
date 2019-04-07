package br.ce.wcaquino.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import br.ce.wcaquino.servicos.LocacaoServiceTestAsserts;

@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	LocacaoServiceTest.class,
	LocacaoServiceTestAsserts.class
})
public class SuiteExecucao {
	//Obrigatório, mas desnecessário.
}
