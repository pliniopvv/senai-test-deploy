package br.com.pvv.senai.model.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.com.pvv.senai.entity.IEntity;
import br.com.pvv.senai.exceptions.DtoToEntityException;
import br.com.pvv.senai.model.dto.annotations.SkipMakeEntity;

public abstract class GenericDto<T extends IEntity> implements IEntity {

	protected abstract Class<T> getType();

	public T makeEntity() throws DtoToEntityException {
		try {
			T entity = (T) getType().getConstructors()[0].newInstance();
			Field[] fieldsDto = this.getClass().getDeclaredFields();
			List<Field> fieldsEntity = List.of(getType().getDeclaredFields());

			for (int i = 0, j = fieldsDto.length; i < j; i++) {
				Field fieldDto = fieldsDto[i];

				var skipField = fieldDto.isAnnotationPresent(SkipMakeEntity.class);
				if (skipField)
					continue;

				Field fieldEntity = fieldsEntity.stream().filter(x -> x.getName().equals(fieldDto.getName())).findAny()
						.get();
				boolean accessEntity = fieldEntity.canAccess(entity);
				boolean accessDto = fieldDto.canAccess(this);

//				if (!fieldDto.isAnnotationPresent(SkipMakeEntity.class)) {
				fieldEntity.setAccessible(true);
				fieldDto.setAccessible(true);

				var value = fieldDto.get(this);
				if (value != null) {
					if (value.getClass().getSuperclass().equals(GenericDto.class)) {
						GenericDto gDto = (GenericDto) value;
						fieldEntity.set(entity, gDto.makeEntity());
					} else {
						fieldEntity.set(entity, value);
					}
				}
//				}

				fieldEntity.setAccessible(accessEntity);
				fieldDto.setAccessible(accessDto);
			}

			return entity;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			throw new DtoToEntityException(e.getMessage());
		}
	}

}
