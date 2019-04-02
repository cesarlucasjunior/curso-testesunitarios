package br.ce.wcaquino.servicos;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
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
	}	
	
	@Test
	public void testeLocacao() throws Exception {
				
		// Cen�rio - instancia classes necess�rias.
		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 3, 24.90);

		// A��o - executo um m�todo que ser� o escopo de teste.
		Locacao locacao = ls.alugarFilme(usuario, filme);
		
		// Valida��o:
		errorCollector.checkThat(locacao.getFilme().getNome(), CoreMatchers.is("Forrest Gump"));
		errorCollector.checkThat(locacao.getUsuario().getNome(), CoreMatchers.is("C�sar Lucas J�nior"));
		errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		errorCollector.checkThat(locacao.getValor(), CoreMatchers.is(24.90));
		errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(new Date(), 1)),
				CoreMatchers.is(true));
		
	}
	
	@Test(expected=Exception.class)
	public void testeLocacaoSemEstoque() throws Exception {
		
		// Cen�rio - instancia classes necess�rias.
		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 0, 24.90);

		// A��o - executo um m�todo que ser� o escopo de teste.
		ls.alugarFilme(usuario, filme);
	}
	
	@Test
	public void testeLocacaoSemEstoqueRobusta() {
		
		// Cen�rio - instancia classes necess�rias.
		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 0, 24.90);
		
		// A��o - executo um m�todo que ser� o escopo de teste.
		try {
			@SuppressWarnings("unused")
			Locacao locacao = ls.alugarFilme(usuario, filme);
			Assert.fail("H� filme no estoque quando n�o deveria ter!");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque!"));
		}		
	}
	
	@Test
	public void testeLocacaoSemEstoqueNova() throws Exception {
		
		// Cen�rio - instancia classes necess�rias.
		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 0, 24.90);

		// A��o - executo um m�todo que ser� o escopo de teste.		
		expectedException.expect(Exception.class);
		expectedException.expectMessage("Filme sem estoque!");	
		
		ls.alugarFilme(usuario, filme);
	}
}
