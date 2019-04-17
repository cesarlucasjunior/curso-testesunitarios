package br.ce.wcaquino.builders;

import java.util.Arrays;
import java.util.Date;

import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoBuilder {
	
	private Locacao locacao;
	
	private LocacaoBuilder() {}
	
	public static LocacaoBuilder umaLocacao() {
		LocacaoBuilder lb = new LocacaoBuilder();
		
		lb.locacao = new Locacao();
		lb.locacao.setDataLocacao(new Date());
		lb.locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
		lb.locacao.setFilme(Arrays.asList(FilmeBuilder.umFilme().agora()));
		lb.locacao.setUsuario(UsuarioBuilder.umUsuario().agora());
		lb.locacao.setValor(4.0);
		
		return lb;
	}
	
	public Locacao agora() {
		return locacao;
	}
	
	public LocacaoBuilder comDataRetorno(Date data) {
		locacao.setDataRetorno(data);
		return this;
	}

}
