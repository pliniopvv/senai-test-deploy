package br.com.pvv.senai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import br.com.pvv.senai.entity.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>, QueryByExampleExecutor<Consulta> {
	List<Consulta> findByPatientId(long id);
}
