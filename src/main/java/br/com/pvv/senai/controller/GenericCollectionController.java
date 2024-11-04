package br.com.pvv.senai.controller;

import br.com.pvv.senai.entity.ICollection;
import br.com.pvv.senai.model.dto.GenericDto;

public abstract class GenericCollectionController<U extends GenericDto<T>, T extends ICollection>
		extends GenericController<U, T> {

}
