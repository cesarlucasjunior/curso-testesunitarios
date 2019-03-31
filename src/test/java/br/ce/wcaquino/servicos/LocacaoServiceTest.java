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

		// Ação - executo um método que será o escopo de teste.
		LocacaoService ls = new LocacaoService();
		Locacao locacao = ls.alugarFilme(usuario, filme);

		// Validações:
		Assert.assertTrue(locacao.getValor() == 24.90); //Verifica se a *expressão* retorna true.
		Assert.assertFalse("Valor locação maior que o esperado!", locacao.getValor() > 24.91);
		Assert.assertTrue(locacao.getUsuario().getNome().contains("Júnior"));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		
		
		Assert.assertEquals(24.90, locacao.getValor(), 0.01); //Verifica dois valores com possível variação.
		Assert.assertNotEquals(Double.valueOf(24.91), Double.valueOf(locacao.getValor()));
		//Válido, mas não recomendado -> assertTrue();
		Assert.assertEquals("César Lucas Júnior", locacao.getUsuario().getNome());
		Assert.assertEquals("Erro comparação objeto!", usuario, usuario2);
		//Verificando se os objetos possuem o mesmo endereço de memória:
		Assert.assertNotSame(usuario, usuario2);		
		Assert.assertNotSame(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1));
		
		//A sacada do assertThat() é utilizar o Matcher que dá outras opções de testes no valor esperado: 
		Assert.assertThat(locacao.getUsuario().getNome(), CoreMatchers.is("César Lucas Júnior"));
		Assert.assertThat(locacao.getFilme().getNome(), CoreMatchers.is("Forrest Gump"));		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(24.90)));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));
	}
}
