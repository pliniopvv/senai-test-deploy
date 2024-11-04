package br.com.pvv.senai.controller;

import br.com.pvv.senai.controller.filter.IFilter;
import br.com.pvv.senai.controller.filter.UsuarioFilter;
import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.exceptions.*;
import br.com.pvv.senai.model.dto.UsuarioDto;
import br.com.pvv.senai.model.dto.UsuarioDtoMinimal;
import br.com.pvv.senai.model.dto.UsuarioUpdateDto;
import br.com.pvv.senai.security.UsuarioService;
import br.com.pvv.senai.service.GenericService;
import br.com.pvv.senai.service.PacienteService;
import br.com.pvv.senai.utils.SenhaUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@Autowired
	private PacienteService pacienteService;

	public GenericService<Usuario> getService() {
		return service;
	}

	public IFilter<Usuario> filterBuilder(Map<String, String> params) throws Exception {
		return new UsuarioFilter(params);
	}

	@GetMapping
	@Operation(summary = "Listar usuários", description = "Consulta a lista de usuários disponiveis no sistema", security = {
			@SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Page<Usuario>> list(
			@Parameter(description = "Dados para filtrar auxiliar a filtrar a consulta") @RequestParam Map<String, String> params)
			throws Exception {
		if (params.size() != 0) {
			var filter = this.filterBuilder(params);
			var list = service.paged(filter.example(), filter.getPagination());
			if (list.hasContent())
				return ResponseEntity.ok(list);
		} else {
			var full_list = getService().all();
			List<Usuario> list = new ArrayList<>();
			if (full_list.size() > 0) {
				for (var user : full_list)
					list.add(user);
				PageImpl<Usuario> p = new PageImpl<Usuario>(list);
				return ResponseEntity.ok(p);
			}
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/me")
	@Operation(summary = "Perfil logado", description = "Retorna os dados do usuário logado no sistema.")
	public ResponseEntity me(Principal principal) {
		var usuario = service.findByEmail(principal.getName());
		if (usuario.isEmpty())
			throw new UsuarioNotFoundException();
		var entity = usuario.get();
		var patient = pacienteService.findByEmail(entity.getEmail());
		if (patient != null)
			entity.setPaciente(patient);
		return ResponseEntity.ok(usuario);
	}

	@PostMapping("pre-registro")
	@Operation(summary = "Cadastrar usuário", description = "Realiza o cadastro de usuário novo no sistema", security = {
			@SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Usuario> register(
			@Parameter(description = "Dados do novo usuário a ser cadastrado") @RequestBody @Valid UsuarioDtoMinimal model)
			throws MethodArgumentNotValidException, DtoToEntityException, BadRequestException,
			EmailViolationExistentException {
		if (model.getPerfil() != Perfil.MEDICO && model.getPerfil() != Perfil.ADMIN)
			throw new BadRequestException("Perfil não autorizado.");

		var entity = model.makeEntity();
		entity.setSenhaMascarada(SenhaUtils.gerarSenhaMascarada(model.getPassword()));
		entity.setPassword(new BCryptPasswordEncoder().encode(model.getPassword()));
		try {
			entity = service.create(entity);
		} catch (DataIntegrityViolationException ex) {
			throw new EmailViolationExistentException();
		}
		entity.setPassword(null);
		return ResponseEntity.status(201).body(entity);
	}

	@PostMapping
	@Operation(summary = "Cadastra novo usuário", description = "Realiza o cadastro de usuário novo no sistema.", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity post(
			@Parameter(description = "Dados do novo usuário a ser cadastrado")
			@Valid @RequestBody UsuarioDto model)
			throws DtoToEntityException, NotAuthorizedException, Exception {
		if (model.getPerfil() == Perfil.PACIENTE)
			throw new NotAuthorizedException();
		var entity = model.makeEntity();
		entity.setPassword(new BCryptPasswordEncoder().encode(model.getPassword()));
		entity.setSenhaMascarada(SenhaUtils.gerarSenhaMascarada(model.getPassword()));
		try {
			entity = service.create(entity);
		} catch (DataIntegrityViolationException ex) {
			throw new EmailViolationExistentException();
		}
		return ResponseEntity.status(201).body(entity);
	}

	@PutMapping("email/{email}/redefinir-senha")
	@Operation(summary = "Alterar senha", description = "Realiza a alteração da senha do usuário informado")
	public ResponseEntity changePassword(Principal principal,
			@Parameter(description = "Nova senha a ser atualizada")
			@NotNull @Valid @Size(max = 255) @RequestBody String password,
			@Parameter(description = "E-mail referente ao usuário cuja senha será atualizada")
			@PathVariable @Valid @Email(message = "E-mail inválido") @NotEmpty(message = "Campo de e-mail necessário") String email)
			throws UsuarioNotFoundException, MethodArgumentNotValidException, HttpMessageNotReadableException,
			UnauthorizationException {

		var oUsuario = service.findByEmail(email);
		if (oUsuario.isEmpty())
			throw new UsuarioNotFoundException();

		var usuario = oUsuario.get();
		usuario.setPassword(new BCryptPasswordEncoder().encode(password));
		service.alter(usuario.getId(), usuario);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	@Operation(summary = "Consultar Usuário", description = "Consulta dados de determinado usuário", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Usuario> get(
			@Parameter(description = "Identificador do usuário")
			@PathVariable(name = "id") Long id) {
		var usuario = service.get(id);
		if (usuario == null) {
			throw new UsuarioNotFoundException();
		}

		return ResponseEntity.ok(usuario);
	}

	@PutMapping("{id}")
	@Operation(summary = "Atualizar Usuário", description = "Atualizar dados de determinado usuário", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Usuario> put(
			@Parameter(description = "Identificador do usuário")
			@PathVariable(name = "id") Long id,
			@Parameter(description = "Dados do usuário a ser atualizado.")
			@Valid @RequestBody UsuarioUpdateDto model) {
		Usuario usuarioExistente = getService().get(id);
		if (getService().get(id) == null) {
			return ResponseEntity.notFound().build();
		}
		Usuario usuarioAtualizado = model.makeEntity();
		usuarioAtualizado.setPerfil(usuarioExistente.getPerfil());
		usuarioAtualizado.setPassword(usuarioExistente.getPassword());
		usuarioAtualizado.setSenhaMascarada(usuarioExistente.getSenhaMascarada());
		Usuario usuarioSalvo;
		try {
			usuarioSalvo = getService().alter(id, usuarioAtualizado);
		} catch (DataIntegrityViolationException ex) {
			throw new EmailViolationExistentException();
		}
		return ResponseEntity.ok(usuarioSalvo);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Excluir usuário", description = "Realiza a exclusão dos dados de determinado usuário", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity delete(
			@Parameter(description = "Identificador do usuário a ser excluído")
			@PathVariable Long id) {
		if (getService().delete(id))
			return ResponseEntity.noContent().build();
		return ResponseEntity.notFound().build();
	}

}
