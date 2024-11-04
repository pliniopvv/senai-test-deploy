package br.com.pvv.senai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.pvv.senai.entity.Paciente;
import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.repository.PacienteRepository;
import br.com.pvv.senai.security.UsuarioService;

@Service
public class PacienteService extends GenericService<Paciente> {

	@Autowired
	private PacienteRepository repository;

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private UsuarioService userService;

	@Override
	public JpaRepository<Paciente, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Paciente create(Paciente model) {
		var endereco = this.enderecoService.create(model.getAddress());
		var usuario = this.userService.findByEmail(model.getEmail()).get();
		model.setUsuario(usuario);
		var retorno = super.create(model);
		return retorno;
	}

	@Override
	public Paciente alter(long id, Paciente model) {
		userService.getRepository().flush();
		model.setId(id);
		var endereco = model.getAddress();
		if (endereco != null && endereco.getId() == 0)
			endereco = this.enderecoService.create(model.getAddress());
		Usuario userAntigo = this.repository.findById(id).get().getUsuario();
		var usuario = this.userService.findByEmail(userAntigo.getEmail()).get();

		String cpfLimpo = model.getCPF().replaceAll("[^\\d]", "");
		usuario.setPerfil(Perfil.PACIENTE);
		usuario.setEmail(model.getEmail());
		usuario.setPassword( new BCryptPasswordEncoder().encode(cpfLimpo));
		usuario.setNome(model.getName());
		usuario.setTelefone(model.getPhone());
		usuario.setDataNascimento(model.getBirthDate());
		usuario.setCpf(cpfLimpo);
		userService.alter(usuario.getId(), usuario);
		model.setUsuario(usuario);
		userService.getRepository().flush();
		
		var retorno = super.alter(id ,model);
		return retorno;
	}

	public Paciente findByEmail(String email) {
		var paciente = repository.findByEmail(email);
		return paciente;
	}

	public long count() {
		return repository.count();
	}

}
