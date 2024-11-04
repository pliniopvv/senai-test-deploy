package br.com.pvv.senai.controller;

import br.com.pvv.senai.controller.filter.ExameFilter;
import br.com.pvv.senai.controller.filter.IFilter;
import br.com.pvv.senai.entity.Exame;
import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.exceptions.DtoToEntityException;
import br.com.pvv.senai.exceptions.ExameNotFoundException;
import br.com.pvv.senai.exceptions.PacienteNotFoundException;
import br.com.pvv.senai.model.dto.ExameDto;
import br.com.pvv.senai.security.UsuarioService;
import br.com.pvv.senai.service.ExameService;
import br.com.pvv.senai.service.GenericService;
import br.com.pvv.senai.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/exames")
public class ExameController extends GenericController<ExameDto, Exame> {

	@Autowired
	private ExameService service;

	@Autowired
	private PacienteService patientService;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public GenericService<Exame> getService() {
		return service;
	}

	@Override
	public IFilter<Exame> filterBuilder(Map<String, String> params) throws Exception {
		return new ExameFilter(params);
	}

	@Override
	@Operation(summary = "Cadastro de exames", description = "Realizar o cadastro da entidade (exame) no banco de dados", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity post(
			@Parameter(description = "Dados referentes a entidade que serão armazenados no banco de dados", required = true) @Valid ExameDto model)
			throws DtoToEntityException, Exception {
		var patient = patientService.get(model.getPatientId());
		if (patient == null)
			throw new PacienteNotFoundException(model.getPatientId());

		var entity = model.makeEntity();
		entity.setPaciente(patient);

		entity = getService().create(entity);
		return ResponseEntity.status(201).body(entity);
	}

	@Override
	@Operation(summary = "Atualização de exames", description = "Realizar a atualização da entidade (exame) no banco de dados", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity put(
			@Parameter(description = "Identificador da entidade (exame) a ser atualizado.", required = true) Long id,
			@Parameter(description = "Novo conteúdo da entidade a ser atualizado.", required = true) @Valid ExameDto model)
			throws DtoToEntityException, ExameNotFoundException, PacienteNotFoundException {

		Exame existingExame = getService().get(id);
		if (existingExame == null) {
			throw new ExameNotFoundException();
		}

		var patient = patientService.get(model.getPatientId());
		if (patient == null)
			throw new PacienteNotFoundException(model.getPatientId());

		var entity = model.makeEntity();
		entity.setPaciente(patient);

		entity = getService().alter(id, entity);
		entity.setPaciente(null);
		return ResponseEntity.ok(entity);
	}

	@Override
	@Operation(summary = "Consultar exame", description = "Realiza a consulta de determinado exame", security = {
			@SecurityRequirement(name = "bearer-key") })
	public ResponseEntity get(@Parameter(description = "Identificador do exame a ser consultado") Long id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Usuario usuarioAutenticado = usuarioService.findByEmail(username).orElse(null);
		if (usuarioAutenticado == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		var retorno = getService().get(id);
		if (retorno == null)
			return ResponseEntity.notFound().build();

		if (usuarioAutenticado.getPerfil() == Perfil.PACIENTE) {
			if (!Long.valueOf(usuarioAutenticado.getPaciente().getId()).equals(retorno.getPaciente().getId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}

		retorno.setPaciente(null);
		return ResponseEntity.ok(retorno);
	}

	@Override
	@Operation(summary = "Listagem paginada dos exames", description = "Obtém a listagem de exames disponível e armazenada no banco de dados.", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Page<Exame>> list(
			@Parameter(description = "Parâmetros de filtro disponíveis para filtragem") @RequestParam Map<String, String> params)
			throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Usuario usuarioAutenticado = usuarioService.findByEmail(username).orElse(null);
		if (usuarioAutenticado == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (usuarioAutenticado.getPerfil().equals(Perfil.PACIENTE)) {
			long pacienteId = usuarioAutenticado.getPaciente().getId();
			params.put("patientId", Long.toString(pacienteId));
		}

		if (!params.isEmpty()) {
			var filter = this.filterBuilder(params);
			var list = getService().paged(filter.example(), filter.getPagination());
			if (list.hasContent())
				return ResponseEntity.ok(list);
		} else {
			var list = getService().all();
			if (!list.isEmpty()) {
				PageImpl<Exame> p = new PageImpl<>(list);
				return ResponseEntity.ok(p);
			}
		}
		return ResponseEntity.notFound().build();
	}


}
