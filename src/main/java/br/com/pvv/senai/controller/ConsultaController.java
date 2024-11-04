package br.com.pvv.senai.controller;

import br.com.pvv.senai.controller.filter.ConsultaFilter;
import br.com.pvv.senai.controller.filter.IFilter;
import br.com.pvv.senai.entity.Consulta;
import br.com.pvv.senai.entity.Paciente;
import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.exceptions.DtoToEntityException;
import br.com.pvv.senai.exceptions.NotRequiredByProjectException;
import br.com.pvv.senai.exceptions.PacienteNotFoundException;
import br.com.pvv.senai.model.dto.ConsultaDto;
import br.com.pvv.senai.security.UsuarioService;
import br.com.pvv.senai.service.ConsultaService;
import br.com.pvv.senai.service.GenericService;
import br.com.pvv.senai.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/consultas")
public class ConsultaController extends GenericController<ConsultaDto, Consulta> {

	private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);

	@Autowired
	ConsultaService service;

	@Autowired
	private PacienteService patientService;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public GenericService<Consulta> getService() {
		return service;
	}

	@Override
	public IFilter<Consulta> filterBuilder(Map<String, String> params) throws NotRequiredByProjectException {
		return new ConsultaFilter(params);
	}

	@Override
	@Operation(summary = "Cadastrar consulta", description = "Realiza o cadastro da entidade consulta.", security = {
			@SecurityRequirement(name = "bearer-key") })
	public ResponseEntity post(@Parameter(description = "Dados da consulta a ser cadastrada.") @Valid ConsultaDto model)
			throws DtoToEntityException, Exception {
		var id = model.getPatientId();
		var patient = patientService.get(id);
		if (patient == null)
			throw new PacienteNotFoundException(id);
		var entity = model.makeEntity();

		entity.setPatient(patient);
		entity = getService().create(entity);
		return ResponseEntity.status(201).body(entity);
	}

	@Override
	@Operation(summary = "Consultar consulta", description = "Realiza a consulta de determinada consulta", security = {
			@SecurityRequirement(name = "bearer-key") })
	public ResponseEntity get(@Parameter(description = "Identificador da consulta a ser consultada") Long id) {

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
			if (!Long.valueOf(usuarioAutenticado.getPaciente().getId()).equals(retorno.getPatient().getId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}

		retorno.setPatient(null);
		return ResponseEntity.ok(retorno);
	}

	@Override
	@Operation(summary = "Atualiza consulta", description = "Realiza a atualização de determinada consulta", security = {
			@SecurityRequirement(name = "bearer-key") })
	public ResponseEntity put(@Parameter(description = "Identificador da consulta a ser atualizada") Long id,
			@Parameter(description = "Dados da consulta a serem atualizados") @Valid ConsultaDto model)
			throws DtoToEntityException, PacienteNotFoundException {
		if (getService().get(id) == null)
			return ResponseEntity.notFound().build();

		Paciente patient = patientService.get(model.getPatientId());
		if (patient == null)
			throw new PacienteNotFoundException(model.getPatientId());

		var entity = model.makeEntity();
		entity.setPatient(patient);

		entity = getService().alter(id, entity);
		entity.setPatient(patient);
		return ResponseEntity.ok(entity);
	}

	@Override
	@Operation(summary = "Listagem paginada das consultas", description = "Obtém a listagem das consultas disponível e armazenada no banco de dados.", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Page<Consulta>> list(
			@Parameter(description = "Parâmetros de filtro disponíveis para filtragem") @RequestParam Map<String, String> params)
			throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Usuario usuarioAutenticado = usuarioService.findByEmail(username).orElse(null);
		if (usuarioAutenticado == null) {
			logger.warn("Pessoa usuária não autenticada");
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
				logger.info("Tamanho da lista de consultas encontradas: " + list.getSize());
				return ResponseEntity.ok(list);
		} else {
			var list = getService().all();
			if (!list.isEmpty()) {
				logger.info("Total de consultas: " + list.size());
				PageImpl<Consulta> p = new PageImpl<>(list);
				return ResponseEntity.ok(p);
			}
		}
		return ResponseEntity.notFound().build();
	}

}
