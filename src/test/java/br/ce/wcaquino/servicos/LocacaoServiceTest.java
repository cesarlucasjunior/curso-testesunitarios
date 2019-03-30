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
		// Cenário - instancia classes necessárias.

		Usuario usuario = new Usuario("César Lucas Júnior");
		Usuario usuario2 = new Usuario("César Lucas Júnior");
		Filme filme = new Filme("Forrest Gump", 3, 24.90);
		
		//Ação
		LocacaoService ls = new LocacaoService();
		Locacao locacao = ls.alugarFilme(usuario, filme);

//		Assert.assertTrue(locacao.getValor() == 24.90);
//		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
//		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(24.90)));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));
		
		
		//Outros exemplos:
		
//		Assert.assertFalse(locacao.getValor() == 25.90);
//		Assert.assertEquals(1, 1);
//		Assert.assertEquals(locacao.getValor(), 24.90, 0.01);
//		
//		Integer idade = 25;
//		int idadeP = 25;
//		
//		Assert.assertEquals(idade, Integer.valueOf(idadeP));
//		Assert.assertEquals(idade.intValue(), idadeP);
//		
//		Assert.assertEquals(usuario, usuario2);
//		
//		//Comparando String sem levar em consideração se ela está em letra maiúscula ou minúscula:
//		
//		Assert.assertTrue("césar lucas júnior".equalsIgnoreCase(usuario.getNome()));
//		Assert.assertTrue("CÉSAR LUCAS JÚNIOR".equalsIgnoreCase(usuario2.getNome()));
//		Assert.assertTrue(usuario.getNome().contains("Jún"));
//		
//		Assert.assertNotNull(usuario);
	}
}
