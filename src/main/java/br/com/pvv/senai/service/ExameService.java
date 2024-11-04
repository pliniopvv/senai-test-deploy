package br.com.pvv.senai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.pvv.senai.entity.Exame;
import br.com.pvv.senai.repository.ExameRepository;

@Service
public class ExameService extends GenericService<Exame> {

	@Autowired
	private ExameRepository repository;

	@Autowired
	private PacienteService pacienteService;

	@Override
	public JpaRepository<Exame, Long> getRepository() {
		return repository;
	}

	@Override
	public Exame create(Exame model) {
		var paciente = pacienteService.findByEmail(model.getPaciente().getEmail()); // paciente já cadastrado - operação
																					// DEVE ter sucesso.
		model.setPaciente(paciente);
		return super.create(model);
	}

	@Override
	public Exame alter(long id, Exame model) {
		var paciente = pacienteService.findByEmail(model.getPaciente().getEmail()); // paciente já cadastrado - operação
																					// DEVE ter sucesso.
		model.setPaciente(paciente);
		return super.alter(id, model);
	}

	public List<Exame> findByPacienteId(long id) {
		return repository.findByPacienteId(id);
	}

	public long count() {
		return repository.count();
	}

}
