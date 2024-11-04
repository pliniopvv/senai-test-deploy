package br.com.pvv.senai.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.pvv.senai.entity.IEntity;

@Service
public abstract class GenericService<T extends IEntity> {

	public abstract JpaRepository<T, Long> getRepository();

	public List<T> all() {
		var retorno = this.getRepository().findAll();
		return retorno;
	}

	public Page<T> paged(Example<T> example, Pageable pageable) {
		return getRepository().findAll(example, pageable);
	}

	public T create(T model) {
		return this.getRepository().save(model);
	}

	public T get(long id) {
		var optional = this.getRepository().findById(id);
		if (optional.isEmpty()) return null;
		return optional.get();
	}

	public T alter(long id, T model) {
		model.setId(id);
		return this.getRepository().save(model);
	}

	public boolean delete(long id) {
		Optional<T> model = this.getRepository().findById(id);
		if (model.isPresent())
			this.getRepository().delete(model.get());
		else
			return false;
		return true;
	}

	public boolean delete(T model) {
		if (this.getRepository().existsById(model.getId()))
			this.getRepository().delete(model);
		else
			return false;
		return true;
	}

}
