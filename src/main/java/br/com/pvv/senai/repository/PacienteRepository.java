package br.com.pvv.senai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import br.com.pvv.senai.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, QueryByExampleExecutor<Paciente> {
	Paciente findByEmail(String email);
}
