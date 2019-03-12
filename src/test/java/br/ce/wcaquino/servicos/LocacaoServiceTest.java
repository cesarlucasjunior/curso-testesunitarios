package br.ce.wcaquino.servicos;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Test
	public void teste() {
		// Cen�rio - instancia classes necess�rias.

		Usuario usuario = new Usuario("C�sar Lucas J�nior");
		Usuario usuario2 = new Usuario("C�sar Lucas J�nior");
		Filme filme = new Filme("Forrest Gump", 3, 24.90);

		LocacaoService ls = new LocacaoService();
		Locacao locacao = ls.alugarFilme(usuario, filme);

		Assert.assertTrue(locacao.getValor() == 24.90);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		
		//Outros exemplos:
		
		Assert.assertFalse(locacao.getValor() == 25.90);
		Assert.assertEquals(1, 1);
		Assert.assertEquals(locacao.getValor(), 24.90, 0.01);
		
		Integer idade = 25;
		int idadeP = 25;
		
		Assert.assertEquals(idade, Integer.valueOf(idadeP));
		Assert.assertEquals(idade.intValue(), idadeP);
		
		Assert.assertEquals(usuario, usuario2);
		
		//Comparando String sem levar em considera��o se ela est� em letra mai�scula ou min�scula:
		
		Assert.assertTrue("c�sar lucas j�nior".equalsIgnoreCase(usuario.getNome()));
		Assert.assertTrue("C�SAR LUCAS J�NIOR".equalsIgnoreCase(usuario2.getNome()));
		Assert.assertTrue(usuario.getNome().contains("J�n"));
		
		//Comparar objetos nulos:		
		Usuario u3 = null;
		
		Assert.assertNotNull(usuario);
	}
}
