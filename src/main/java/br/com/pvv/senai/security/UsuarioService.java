package br.com.pvv.senai.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.repository.UserRepository;
import br.com.pvv.senai.service.GenericService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;

@Service
public class UsuarioService extends GenericService<Usuario> {

	@Autowired
	private UserRepository repository;

	@Autowired
	private EntityManager em;

	@Override
	public Page<Usuario> paged(Example<Usuario> example, Pageable pageable) {
		var cb = em.getCriteriaBuilder();
		var q = cb.createQuery(Usuario.class);
		var p = q.from(Usuario.class);

		var userExample = example.getProbe();

		cb.notEqual(p.get("perfil"), Perfil.PACIENTE);
		List<Predicate> predicates = new ArrayList<>();

		if (userExample.getId() != 0)
			predicates.add(cb.equal(p.get("id"), userExample.getId()));
		if (userExample.getEmail() != null)
			predicates.add(cb.like(p.get("email"), "%" + userExample.getEmail() + "%"));
		if (userExample.getNome() != null)
			predicates.add(cb.like(p.get("nome"), "%" + userExample.getNome() + "%"));

		Predicate withWhere = null;
		if (predicates.size() == 1)
			withWhere = predicates.get(0);
		else
			withWhere = cb.or(predicates.toArray(new Predicate[predicates.size()]));

		CriteriaQuery<Usuario> select = null;
		if (withWhere != null)
			select = q.where(withWhere).select(p);
		else
			select = q.select(p);

		var result = em.createQuery(select).getResultList();

		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public JpaRepository<Usuario, Long> getRepository() {
		return this.repository;
	}

	public boolean has(String email) {
		var example = makeExample(email);
		return this.repository.exists(example);
	}

	public Optional<Usuario> findByEmail(String email) {
		var example = makeExample(email);
		return this.repository.findOne(example);
	}

	private Example<Usuario> makeExample(String email) {
		ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnoreNullValues();
		Usuario u = new Usuario();
		u.setEmail(email);
		Example example = Example.of(u, matcher);
		return example;
	}

	public long count() {
		return repository.count();
	}
}
