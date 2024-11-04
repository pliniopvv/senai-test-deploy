package br.com.pvv.senai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.pvv.senai.entity.Endereco;
import br.com.pvv.senai.repository.EnderecoRepository;

@Service
public class EnderecoService extends GenericService<Endereco> {

	@Autowired
	private EnderecoRepository repository;
	
	@Override
	public JpaRepository<Endereco, Long> getRepository() {
		return this.repository;
	}

}
