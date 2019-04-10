package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {
	
	private Usuario usuario;
	
	private UsuarioBuilder() {}
	
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder usuarioBuilder = new UsuarioBuilder();
		
		usuarioBuilder.usuario = new Usuario();
		usuarioBuilder.usuario.setNome("Usuario 1");
		
		return usuarioBuilder;
	}
	
	public Usuario agora() {
		return usuario;
	}
	
	public UsuarioBuilder comNomeParametrizado(String nome) {
		usuario.setNome(nome);
		return this;
	}

}
