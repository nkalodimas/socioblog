package gr.uoa.di.scan.thesis.controller;

import gr.uoa.di.scan.thesis.entity.Identifiable;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;
import gr.uoa.di.scan.thesis.service.GenericService;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public abstract class GenericController<T, DTO extends Identifiable<ID>, ID extends Serializable > {
	
	abstract GenericService<T,DTO, ID> getService();

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody DTO create(@RequestBody DTO dto) {
		System.out.println("Responding on endpoint request /api/entity POST request");
		return getService().create(dto);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody DTO get(@PathVariable ID id) {
		System.out.println("Responding on endpoint request /api/entity/id GET request");
		return getService().findByID(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody List<DTO> findAll() {
		System.out.println("Responding on endpoint request /api/entity GET request");
		return getService().findAll();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT, consumes="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody DTO update(@PathVariable ID id, @RequestBody DTO dto) {
		System.out.println("Responding on endpoint request /api/entity PUT request");
		return getService().update(dto);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	@ResponseStatus( value = HttpStatus.OK)
	public void delete(@PathVariable ID id) {
		System.out.println("Responding on endpoint request /api/entity/id DELETE request");
		getService().delete(id);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody String handleEntityNotFoundException(EntityNotFoundException e) {
		System.out.println(e.getMessage());
		return e.getMessage();
	}

}
