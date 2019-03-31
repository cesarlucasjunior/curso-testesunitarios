package br.ce.wcaquino.servicos;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Test
	public void testeLocacao() {

		// Cen�rio - instancia classes necess�rias.
		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 3, 24.90);

		// A��o - executo um m�todo que ser� o escopo de teste.
		LocacaoService ls = new LocacaoService();
		Locacao locacao = ls.alugarFilme(usuario, filme);

		// Valida��es:
		Assert.assertTrue(locacao.getValor() == 24.90); //Verifica se a *express�o* retorna true.
		Assert.assertFalse("Valor loca��o maior que o esperado!", locacao.getValor() > 24.91);
		Assert.assertTrue(locacao.getUsuario().getNome().contains("J�nior"));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		
		
		Assert.assertEquals(24.90, locacao.getValor(), 0.01); //Verifica dois valores com poss�vel varia��o.
		Assert.assertNotEquals(Double.valueOf(24.91), Double.valueOf(locacao.getValor()));
		//V�lido, mas n�o recomendado -> assertTrue();
		Assert.assertEquals("C�sar Lucas J�nior", locacao.getUsuario().getNome());
		Assert.assertEquals("Erro compara��o objeto!", usuario, usuario2);
		//Verificando se os objetos possuem o mesmo endere�o de mem�ria:
		Assert.assertNotSame(usuario, usuario2);		
		Assert.assertNotSame(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1));
		
		//A sacada do assertThat() � utilizar o Matcher que d� outras op��es de testes no valor esperado: 
		Assert.assertThat(locacao.getUsuario().getNome(), CoreMatchers.is("C�sar Lucas J�nior"));
		Assert.assertThat(locacao.getFilme().getNome(), CoreMatchers.is("Forrest Gump"));		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(24.90)));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));
	}
}
