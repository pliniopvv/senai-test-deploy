package br.com.pvv.senai.controller.filter;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import br.com.pvv.senai.entity.IEntity;

public interface IFilter<T extends IEntity> {
	public Example<T> example();
	public Pageable getPagination();
}
