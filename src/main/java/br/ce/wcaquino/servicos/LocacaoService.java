package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;	
	private SPCService spcService;
	private EmailService emailService;
	
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
		
		
		if(spcService.possuiNegativacao(usuario)) {
			throw new RuntimeException("Usuário negativado!");
		}
		
		
		locacao.setFilme(filmes);
		
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		// Entrega no dia seguinte
		Date dataEntrega = extrairDataEntrega();
		
		locacao.setDataRetorno(dataEntrega);
		
		// Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}

	private Date extrairDataEntrega() {
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		return dataEntrega;
	}
	
	public void notificarAtraso() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
			for(Locacao locacao : locacoes) {
				if(locacao.getDataRetorno().before(new Date())) {
					emailService.notificarAtraso(locacao.getUsuario());
				}
			}
	}
	
	public void prorrogarLocacao(Locacao locacaoAntiga, int diasAMais) {
		Locacao locacao = new Locacao();
		locacao.setUsuario(locacaoAntiga.getUsuario());
		locacao.setFilme(locacaoAntiga.getFilme());
		locacao.setDataLocacao(new Date());
		locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(diasAMais));
		locacao.setValor(locacaoAntiga.getValor()*diasAMais);
		dao.salvar(locacao);
	}
}