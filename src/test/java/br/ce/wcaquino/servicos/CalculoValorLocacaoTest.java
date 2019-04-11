package br.ce.wcaquino.servicos;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Parameter
	public List<Filme> filmes;
	@Parameter(value=1)
	public Double valorLocacao;
	
	public LocacaoService ls;
	
	@Parameter(value=2)
	public String cenario;	
	
	@Before
	public void setup() {
		ls = new LocacaoService();
		LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
		ls.setLocacaoDAO(dao);
	}
	
	private static Filme filme1 = new Filme("Filme 1", 1, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 3, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 4, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 5, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 6, 4.0);
	
	
	//Definindo conjunto de testes que serão testados:
	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][]{
			{Arrays.asList(filme1,filme2), 8.0, "2 Filmes : 0%"},
			{Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes : 25%"},
			{Arrays.asList(filme1, filme2, filme3, filme4), 12.25, "4 Filmes : 50%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 11.6875, "5 Filmes : 75%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 10.015625, "6 Filmes : 100%"},			
		});
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
		//cenario
		Usuario u1 = new Usuario("César Lucas Júnior");
		
		//acao
		Locacao locacao = ls.alugarFilme(u1, filmes);
		
		//verificacao
		error.checkThat(locacao.getValor(), CoreMatchers.is(valorLocacao));
	}
}
