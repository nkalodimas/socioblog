package gr.uoa.di.scan.thesis.service;

import gr.uoa.di.scan.thesis.entity.Identifiable;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericServiceBase<T, DTO extends Identifiable<ID>, ID extends Serializable> implements GenericService<T, DTO, ID>{
		
	abstract JpaRepository<T, ID> getRepository();
	abstract Class<T> getTypeofEntity();
	abstract Class<DTO> getTypeofDTO();
	
	@Autowired
	protected DozerBeanMapper mapper;
	
	@Transactional
	public DTO create(DTO dto) {
		return mapper.map(getRepository().save( mapper.map(dto, getTypeofEntity()) ), getTypeofDTO());
	}

	@Transactional()
	public DTO findByID(ID id) {
		
		T entity = getRepository().findOne(id);
		if (entity == null)
			return null;
		return mapper.map(entity, getTypeofDTO());
	}
	
	@Transactional
	public List<DTO> findAll() {
		
		List<DTO> list = new ArrayList<DTO>();
		for (T entity : getRepository().findAll()){
			list.add(mapper.map(entity, getTypeofDTO()));
		}
		
		return list;
	}
	
	@Transactional(rollbackFor = EntityNotFoundException.class)
	public DTO update(DTO dto) {
		
		if (getRepository().exists(dto.getId()))
			return mapper.map(getRepository().save(mapper.map(dto, getTypeofEntity())),getTypeofDTO());
		
		throw new EntityNotFoundException(getTypeofEntity().getSimpleName() + " not found");
	}
	
	@Transactional(rollbackFor = EntityNotFoundException.class)
	public void delete(ID id) {
		
		T entity = getRepository().findOne(id);

		if (entity == null)
			throw new EntityNotFoundException(getTypeofEntity().getSimpleName() + " not found");

		getRepository().delete(entity);
		getRepository().flush();
	}
}