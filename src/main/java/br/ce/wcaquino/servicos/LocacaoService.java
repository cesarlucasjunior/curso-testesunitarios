package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mockito.Mock;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;	
	private SPCService spcService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		Locacao locacao = new Locacao();
		int qtdFilme = 0;
		
		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new Exception("Filme sem estoque!");
			}
			
			qtdFilme++;
			if(qtdFilme == 3) {
				filme.setPrecoLocacao(filme.getPrecoLocacao() - (filme.getPrecoLocacao() * 0.25));
			}			
			if(qtdFilme == 4) {
				filme.setPrecoLocacao(filme.getPrecoLocacao() - (filme.getPrecoLocacao() * 0.5));
			}
			if(qtdFilme == 5) {
				filme.setPrecoLocacao(filme.getPrecoLocacao() - (filme.getPrecoLocacao() * 0.75));
			}
			if(qtdFilme == 6) {
				filme.setPrecoLocacao(0d);
			}
			locacao.setValor(filme.getPrecoLocacao());
			
		}
		
		
		if(spcService.validaCPF()) {
			throw new RuntimeException();
		}
		
		
		locacao.setFilme(filmes);
		
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		
		locacao.setDataRetorno(dataEntrega);
		
		// Salvando a locacao...	
		dao.salvar();
		
		return locacao;
	}
	
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}

	public void setSpcService(SPCService spc) {
		spcService = spc;
	}
}