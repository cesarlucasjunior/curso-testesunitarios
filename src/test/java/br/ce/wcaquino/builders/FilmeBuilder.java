package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

	private Filme filme;
	
	private FilmeBuilder() {}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder filmeBuilder = new FilmeBuilder();
		
		filmeBuilder.filme = new Filme();
		filmeBuilder.filme.setEstoque(2);
		filmeBuilder.filme.setNome("Forrest Gump");
		filmeBuilder.filme.setPrecoLocacao(4.0);
		
		return filmeBuilder;
	}
	
	public Filme agora() {
		return filme;
	}
	
	public FilmeBuilder comEstoqueParametrizado(Integer numeroEstoque) {
		filme.setEstoque(numeroEstoque);
		return this;
	}
	
}
