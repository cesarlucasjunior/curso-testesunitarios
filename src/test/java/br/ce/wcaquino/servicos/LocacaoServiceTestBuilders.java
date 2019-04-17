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

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocacaoServiceTestBuilders {

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private LocacaoService ls;
	private SPCService spcService;
	private LocacaoDAO dao;
	private EmailService email;

	@Before
	public void setup() {
		ls = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		ls.setLocacaoDAO(dao);
		spcService = Mockito.mock(SPCService.class);
		ls.setSpcService(spcService);
		email = Mockito.mock(EmailService.class);
		ls.setEmailService(email);
	}

	@Test
	public void testeLocacao() throws Exception {

		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenário - instancia classes necessárias.
		Usuario usuario = UsuarioBuilder.umUsuario().comNomeParametrizado("César Lucas Júnior").agora();
		@SuppressWarnings("unused")
		Usuario usuario2 = UsuarioBuilder.umUsuario().agora();
		
		Filme filme = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();

		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		filmes.add(filme2);

		// Ação - executo um método que será o escopo de teste.
		Locacao locacao = ls.alugarFilme(usuario, filmes);

		// Validação:
		errorCollector.checkThat(locacao.getFilme().get(0).getNome(), CoreMatchers.is("Forrest Gump"));
		errorCollector.checkThat(locacao.getUsuario().getNome(), CoreMatchers.is("César Lucas Júnior"));
		errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(8.0));
		errorCollector.checkThat(
				DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(new Date(), 1)),
				CoreMatchers.is(true));
		errorCollector.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		errorCollector.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));

	}

	@Test(expected = Exception.class)
	public void testeLocacaoSemEstoque() throws Exception {

		// Cenário - instancia classes necessárias.
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		@SuppressWarnings("unused")
		Usuario usuario2 = UsuarioBuilder.umUsuario().agora();
		Filme filme = FilmeBuilder.umFilme().comEstoqueParametrizado(0).agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();

		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		filmes.add(filme2);

		// Ação - executo um método que será o escopo de teste.
		ls.alugarFilme(usuario, filmes);
	}

	@Test
	public void testeLocacaoSemEstoqueRobusta() {

		// Cenário - instancia classes necessárias.
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		@SuppressWarnings("unused")
		Usuario usuario2 = UsuarioBuilder.umUsuario().agora();
		Filme filme = FilmeBuilder.umFilme().comEstoqueParametrizado(0).agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();

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
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		@SuppressWarnings("unused")
		Usuario usuario2 = UsuarioBuilder.umUsuario().agora();
		
		Filme filme = FilmeBuilder.umFilme().comEstoqueParametrizado(0).agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();

		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		filmes.add(filme2);

		// Ação - executo um método que será o escopo de teste.
		expectedException.expect(Exception.class);
		expectedException.expectMessage("Filme sem estoque!");

		ls.alugarFilme(usuario, filmes);
	}

	@Test
	// @Ignore
	public void naoDevolverFilmeNoDomingo() throws Exception {

		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		// acao
		Locacao locacao = ls.alugarFilme(usuario, filmes);

		// verificacao
		boolean hojeESegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		errorCollector.checkThat(hojeESegunda, CoreMatchers.is(true));
		Assert.assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		Assert.assertThat(locacao.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		Assert.assertThat(locacao.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}
	
	@Test
	public void naoAlugarParaNegativado() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> listaFilme = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Mockito.when(spcService.possuiNegativacao(usuario)).thenReturn(true);
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Usuário negativado!");
		ls.alugarFilme(usuario, listaFilme);
		
		Mockito.verify(spcService).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadasTest() {
		//cenario
		List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umaLocacao().comDataRetorno(DataUtils.obterDataComDiferencaDias(-2)).agora());
		
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		//acao
		ls.notificarAtraso();
		
		//verificacao
		Mockito.verify(email).notificarAtraso(locacoes.get(0).getUsuario());
	}
}
