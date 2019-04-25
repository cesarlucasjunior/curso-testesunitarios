package br.ce.wcaquino.servicos;

import java.util.ArrayList;
import java.util.Date;

import org.hamcrest.CoreMatchers;
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

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
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
		//errorCollector.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(2));		
	}
}
