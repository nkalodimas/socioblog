package gr.uoa.di.scan.thesis.service;

import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;

import java.io.Serializable;
import java.util.List;

public interface GenericService< T, DTO, ID extends Serializable > {
    
	public DTO create(DTO dto);
	public DTO findByID(ID id);
	public List<DTO> findAll();
	public DTO update(DTO dto) throws EntityNotFoundException;
	public void delete(ID id) throws EntityNotFoundException;
}
