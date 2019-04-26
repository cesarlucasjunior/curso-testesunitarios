package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
public class LocacaoServicePowerMockTest {
	

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();
	
	@InjectMocks
	public LocacaoService ls;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private SPCService spcService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ls = PowerMockito.spy(ls);
	}	
	
	@Test
	public void testeLocacao() throws Exception {
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(26, 04, 2019));
				
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Filme filme = FilmeBuilder.umFilme().agora();
		
		ArrayList<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		// Ação - executo um método que será o escopo de teste.
		Locacao locacao = ls.alugarFilme(usuario, filmes);
		
		// Validação:
		errorCollector.checkThat(locacao.getFilme().get(0).getNome(), CoreMatchers.is("Forrest Gump"));
		errorCollector.checkThat(locacao.getUsuario().getNome(), CoreMatchers.is("Usuario 1"));
		errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(4.0));
		errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(new Date(), 1)),
				CoreMatchers.is(true));
		//errorCollector.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		errorCollector.checkThat(locacao.getDataRetorno(), is(DataUtils.adicionarDias(new Date(), 1)));		
	}
	
	@Test
	public void naoDevolverFilmeNoDomingo() throws Exception {
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(27, 04, 2019));
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
	
	@Test
	public void deveAlugarFilmeSemExtrairDataEntrega() throws Exception {
		//Ambiente
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Forrest Gump", 3, 33.33));
		//Ação
		ls.alugarFilme(usuario, filmes);
		//Teste
		PowerMockito.doReturn(DataUtils.obterData(01, 01, 2020)).when(ls, "extrairDataEntrega");
		
		PowerMockito.verifyPrivate(ls).invoke("extrairDataEntrega");
	}
	
	@Test
	public void extrairDataEntregaTest() throws Exception {
		//Ação
		Date dataRetornada = Whitebox.invokeMethod(ls, "extrairDataEntrega");
		//Teste
		
		Assert.assertThat(dataRetornada, CoreMatchers.is(DataUtils.adicionarDias(new Date(), 1)));
	}
}
