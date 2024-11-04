package br.com.pvv.senai.controller.filter;

import br.com.pvv.senai.entity.Usuario;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class UsuarioFilter implements IFilter<Usuario> {

	private int id;
	private String nome;
	private String email;
	private int pageSize;
	private int pageNumber;

	public UsuarioFilter(Map<String, String> params) {
		if (params.containsKey("id"))
			setId(Integer.valueOf(params.get("id")));
		if (params.containsKey("nome"))
			setNome(params.get("nome"));
		if (params.containsKey("email"))
			setEmail(params.get("email"));
		this.setPageNumber(params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber")) : 0);
		this.setPageSize(params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 10);
	}

	@Override
	public Example<Usuario> example() {

		ExampleMatcher example = ExampleMatcher.matching();

		Usuario usuario = new Usuario();
		if (getId() != 0)
			usuario.setId(getId());
		usuario.setNome(getNome());
		usuario.setEmail(getEmail());

		return Example.of(usuario, example);
	}

	@Override
	public Pageable getPagination() {
		return PageRequest.of(pageNumber, pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
