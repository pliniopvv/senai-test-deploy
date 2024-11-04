package br.com.pvv.senai.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pvv.senai.controller.filter.IFilter;
import br.com.pvv.senai.entity.IEntity;
import br.com.pvv.senai.exceptions.DtoToEntityException;
import br.com.pvv.senai.model.dto.GenericDto;
import br.com.pvv.senai.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
public abstract class GenericController<U extends GenericDto<T>, T extends IEntity> {

	public abstract GenericService<T> getService();

	public abstract IFilter<T> filterBuilder(Map<String, String> params) throws Exception;

	@GetMapping
	@Operation(summary = "Listagem paginada das entidades", description = "Obtém a listagem da referida entidade disponível e armazenada no banco de dados.", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Page<T>> list(
			@Parameter(description = "Parâmetros de filtro disponíveis para filtragem") @RequestParam Map<String, String> params)
			throws Exception {
		if (params.size() != 0) {
			var filter = this.filterBuilder(params);
			var list = getService().paged(filter.example(), filter.getPagination());
			if (list.hasContent())
				return ResponseEntity.ok(list);
		} else {
			var list = getService().all();
			if (list.size() > 0) {
				PageImpl<T> p = new PageImpl<T>(list);
				return ResponseEntity.ok(p);
			}
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("{id}")
	@Operation(summary = "Consulta entidade", description = "Obtém os dados de entidade específica", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity get(
			@Parameter(description = "Identificador da entidade requisitada", required = true) @PathVariable Long id) {
		var retorno = getService().get(id);
		if (retorno == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(retorno);
	}

	@PutMapping("{id}")
	@Operation(summary = "Atualiza entidade", description = "Atualiza os dados de uma entidade específica", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity put(
			@Parameter(description = "Identificador da entidade a ser atualizada.", required = true) @PathVariable Long id,
			@Parameter(description = "Dados da entidade a ser atualizada.", required = true) @Valid @RequestBody U model)
			throws DtoToEntityException, Exception {
		if (getService().get(id) == null)
			return ResponseEntity.notFound().build();
		getService().alter(id, model.makeEntity());
		return ResponseEntity.noContent().build();
	}

	@PostMapping
	@Operation(summary = "Cadastro de entidade", description = "Realiza o cadastro dos dados da entidade", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity post(
			@Parameter(description = "Conteúdo da entidade a ser salvo", required = true) @Valid @RequestBody U model)
			throws DtoToEntityException, Exception {
		var entity = getService().create(model.makeEntity());
		return ResponseEntity.status(201).body(entity);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Apagar entidade", description = "Realiza a exclusão dos dados de determinada entidade", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity delete(
			@Parameter(description = "Identificador da entidade a ser excluída") @PathVariable long id) {
		if (getService().delete(id))
			return ResponseEntity.noContent().build();
		return ResponseEntity.notFound().build();
	}
}
