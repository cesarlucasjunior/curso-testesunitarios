package br.ce.wcaquino.servicos;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();
	
	@Test
	public void testeLocacao() throws Exception {

		// Cen�rio - instancia classes necess�rias.
		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 3, 24.90);

		// A��o - executo um m�todo que ser� o escopo de teste.
		LocacaoService ls = new LocacaoService();
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
		LocacaoService ls = new LocacaoService();
		ls.alugarFilme(usuario, filme);
	}
	
	@Test
	public void testeLocacaoSemEstoqueRobusta() {
		
		// Cen�rio - instancia classes necess�rias.
		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		@SuppressWarnings("unused")
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 1, 24.90);
		
		// A��o - executo um m�todo que ser� o escopo de teste.
		try {
			LocacaoService ls = new LocacaoService();
			Locacao locacao = ls.alugarFilme(usuario, filme);
			Assert.fail("H� filme no estoque quando n�o deveria ter!");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque!"));
		}		
	}
}
