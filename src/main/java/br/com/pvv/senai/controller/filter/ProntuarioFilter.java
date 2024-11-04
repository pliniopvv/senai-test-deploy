package br.com.pvv.senai.controller.filter;

import br.com.pvv.senai.entity.Paciente;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class ProntuarioFilter implements IFilter<Paciente> {

	private String name;
	private String id;
	private int pageSize;
	private int pageNumber;

	public ProntuarioFilter(Map<String, String> params) {
		this.setName(params.get("name"));
		this.setId(params.get("id"));
		this.setPageNumber(params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber")) : 0);
		this.setPageSize(params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 10);
	}

	@Override
	public Example<Paciente> example() {

		ExampleMatcher matcher = ExampleMatcher.matchingAny()
				.withMatcher("name", match -> match.contains().ignoreCase());

		Paciente paciente = new Paciente();

		if (this.getName() != null)
			paciente.setName(this.getName());
		if (this.getName() != null && !this.getName().isEmpty()) {
			paciente.setName(this.getName());
		} else {
			matcher = matcher.withIgnorePaths("name");
		}

		if (this.getId() != null) {
			paciente.setId(Long.valueOf(this.getId()));
		} else {
			matcher = matcher.withIgnorePaths("id");
		}

			return Example.of(paciente, matcher);
	}

	@Override
	public Pageable getPagination() {
		return PageRequest.of(this.pageNumber, this.pageSize);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}
