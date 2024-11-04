package br.com.pvv.senai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.pvv.senai.entity.Consulta;
import br.com.pvv.senai.repository.ConsultaRepository;

@Service
public class ConsultaService extends GenericService<Consulta> {

	@Autowired
	private ConsultaRepository repository;

	@Autowired
	private PacienteService pacienteService;

	@Override
	public JpaRepository<Consulta, Long> getRepository() {
		return repository;
	}

	@Override
	public Consulta create(Consulta model) {
		var paciente = pacienteService.findByEmail(model.getPatient().getEmail());

		model.setPatient(paciente);
		return super.create(model);
	}

	@Override
	public Consulta alter(long id, Consulta model) {
		var paciente = pacienteService.findByEmail(model.getPatient().getEmail());

		model.setPatient(paciente);
		return super.alter(id, model);
	}

	public List<Consulta> findByPacienteId(long id) {
		return repository.findByPatientId(id);
	}

	public long count() {
		return repository.count();
	}

}
