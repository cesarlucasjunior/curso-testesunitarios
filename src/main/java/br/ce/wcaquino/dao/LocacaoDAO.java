package br.ce.wcaquino.dao;

import java.util.List;

import br.ce.wcaquino.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar();
	
	public List<Locacao> obterLocacoesPendentes();
}
