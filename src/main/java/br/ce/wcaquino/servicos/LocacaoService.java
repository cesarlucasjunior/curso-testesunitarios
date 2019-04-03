package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoService {

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		Locacao locacao = new Locacao();
		
		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new Exception("Filme sem estoque!");
			}
		}
		locacao.setFilme(filmes);
		locacao.setValor(filmes.get(0).getPrecoLocacao() + filmes.get(1).getPrecoLocacao());
		
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		// Salvando a locacao...
		// TODO adicionar método para salvar
		return locacao;
	}

}