package gr.uoa.di.scan.thesis.service;

import gr.uoa.di.scan.thesis.dto.TagDTO;
import gr.uoa.di.scan.thesis.entity.Tag;

public interface TagService extends GenericService<Tag, TagDTO, Long>{
	public TagDTO findByTitle(String title);
}
