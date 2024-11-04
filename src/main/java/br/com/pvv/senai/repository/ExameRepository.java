package br.com.pvv.senai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import br.com.pvv.senai.entity.Exame;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long>, QueryByExampleExecutor<Exame> {
	List<Exame> findByPacienteId(long id);
}
