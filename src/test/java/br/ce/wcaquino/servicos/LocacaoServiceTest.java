package br.ce.wcaquino.servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocacaoServiceTest {

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private LocacaoService ls;
	
	
	@Before
	public void setup() {
		ls = new LocacaoService();
		LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
		ls.setLocacaoDAO(dao);
		SPCService spcService = Mockito.mock(SPCService.class);
		ls.setSpcService(spcService);
	}	
	
	@Test
	public void testeLocacao() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
				
		// Cenário - instancia classes necessárias.
		Usuario usuario = new Usuario("César Lucas Júnior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("César Lucas Júnior");
		Filme filme = new Filme("Forrest Gump", 3, 24.90);
		Filme filme2 = new Filme("Código da Vinci", 3, 10.00);
		
		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		filmes.add(filme2);

		// Ação - executo um método que será o escopo de teste.
		Locacao locacao = ls.alugarFilme(usuario, filmes);
		
		// Validação:
		errorCollector.checkThat(locacao.getFilme().get(0).getNome(), CoreMatchers.is("Forrest Gump"));
		errorCollector.checkThat(locacao.getUsuario().getNome(), CoreMatchers.is("César Lucas Júnior"));
		errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(34.90));
		errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(new Date(), 1)),
				CoreMatchers.is(true));
		errorCollector.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		errorCollector.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
		
		
	}
	
	@Test(expected=Exception.class)
	public void testeLocacaoSemEstoque() throws Exception {
		
		// Cenário - instancia classes necessárias.
		Usuario usuario = new Usuario("César Lucas Júnior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("César Lucas Júnior");
		Filme filme = new Filme("Forrest Gump", 0, 24.90);
		Filme filme2 = new Filme("Código da Vinci", 3, 10.00);
		
		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		filmes.add(filme2);


		// Ação - executo um método que será o escopo de teste.
		ls.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void testeLocacaoSemEstoqueRobusta() {
		
		// Cenário - instancia classes necessárias.
		Usuario usuario = new Usuario("César Lucas Júnior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("César Lucas Júnior");
		Filme filme = new Filme("Forrest Gump", 0, 24.90);
		Filme filme2 = new Filme("Código da Vinci", 0, 10.00);
		
		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		filmes.add(filme2);

		
		// Ação - executo um método que será o escopo de teste.
		try {
			@SuppressWarnings("unused")
			Locacao locacao = ls.alugarFilme(usuario, filmes);
			Assert.fail("Há filme no estoque quando não deveria ter!");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque!"));
		}		
	}
	
	@Test
	public void testeLocacaoSemEstoqueNova() throws Exception {
		
		// Cenário - instancia classes necessárias.
		Usuario usuario = new Usuario("César Lucas Júnior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("César Lucas Júnior");
		Filme filme = new Filme("Forrest Gump", 0, 24.90);
		Filme filme2 = new Filme("Código da Vinci", 3, 10.00);
		
		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		filmes.add(filme2);


		// Ação - executo um método que será o escopo de teste.		
		expectedException.expect(Exception.class);
		expectedException.expectMessage("Filme sem estoque!");	
		
		ls.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void desconto25Pct3Filme() throws Exception {
		//cenario
		Usuario u1 = new Usuario("César Lucas Júnior");
		List<Filme> filmes = Arrays.asList(new Filme("Forrest Gump", 1, 10.0),
										   new Filme("Her", 2, 10.0),
										   new Filme("Rocky", 3, 10.0));
		//acao	
		Locacao locacao = ls.alugarFilme(u1, filmes);
		
		//verificacao
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(27.5));
	}

	@Test
	public void desconto50Pct4Filme() throws Exception {
		//cenario
		Usuario u1 = new Usuario("César Lucas Júnior");
		List<Filme> filmes = Arrays.asList(new Filme("Forrest Gump", 1, 10.0),
										   new Filme("Her", 2, 10.0),
										   new Filme("Rocky", 3, 10.0),
										   new Filme("Pantera Negra", 4, 10.0));
		//acao	
		Locacao locacao = ls.alugarFilme(u1, filmes);
		
		//verificacao
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(32.5));
	}
	
	@Test
	public void desconto75Pct5Filme() throws Exception {
		//cenario
		Usuario u1 = new Usuario("César Lucas Júnior");
		List<Filme> filmes = Arrays.asList(new Filme("Forrest Gump", 1, 10.0),
										   new Filme("Her", 2, 10.0),
										   new Filme("Rocky", 3, 10.0),
										   new Filme("Pantera Negra", 4, 10.0),
										   new Filme("Homem de Ferro", 5, 10.0));
		//acao	
		Locacao locacao = ls.alugarFilme(u1, filmes);
		
		//verificacao
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(35.0));
	}
	
	@Test
	public void desconto100Pct6Filme() throws Exception {
		//cenario
		Usuario u1 = new Usuario("César Lucas Júnior");
		List<Filme> filmes = Arrays.asList(new Filme("Forrest Gump", 1, 10.0),
										   new Filme("Her", 2, 10.0),
										   new Filme("Rocky", 3, 10.0),
										   new Filme("Pantera Negra", 4, 10.0),
										   new Filme("Homem de Ferro", 5, 10.0),
										   new Filme("Capitão América", 6, 10.0));
		//acao	
		Locacao locacao = ls.alugarFilme(u1, filmes);
		
		//verificacao
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(35.0));
	}
	
	@Test
	//@Ignore
	public void naoDevolverFilmeNoDomingo() throws Exception {
		
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cenario
		Usuario usuario = new Usuario("César Lucas Júnior");
		List<Filme> filmes = Arrays.asList(new Filme("Forrest Gump", 3, 33.33));
		//acao
		Locacao locacao = ls.alugarFilme(usuario, filmes);
		
		//verificacao
		boolean hojeESegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		errorCollector.checkThat(hojeESegunda, CoreMatchers.is(true));
		Assert.assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		Assert.assertThat(locacao.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		Assert.assertThat(locacao.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}
}
