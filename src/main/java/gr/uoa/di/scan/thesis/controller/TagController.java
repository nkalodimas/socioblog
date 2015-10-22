package gr.uoa.di.scan.thesis.controller;

import gr.uoa.di.scan.thesis.dto.TagDTO;
import gr.uoa.di.scan.thesis.entity.Tag;
import gr.uoa.di.scan.thesis.security.SecurityUtils;
import gr.uoa.di.scan.thesis.service.GenericService;
import gr.uoa.di.scan.thesis.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tags")
public class TagController extends GenericController<Tag,TagDTO, Long>{
	
	@Autowired
	TagService tagService;

	@Autowired
	SecurityUtils securityUtils;

	@Override
	GenericService<Tag,TagDTO, Long> getService() {
		
		return tagService;
	}
	

	@Override
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public TagDTO create(@RequestBody TagDTO dto) {
		return super.create(dto);
	}


	@Override
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(Long id) {
		super.delete(id);
	}

}
