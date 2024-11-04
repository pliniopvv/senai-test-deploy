package br.com.pvv.senai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import br.com.pvv.senai.entity.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>, QueryByExampleExecutor<Endereco> {

}
