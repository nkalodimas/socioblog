package gr.uoa.di.scan.thesis.service;

import gr.uoa.di.scan.thesis.dto.TagDTO;
import gr.uoa.di.scan.thesis.entity.Post;
import gr.uoa.di.scan.thesis.entity.Tag;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;
import gr.uoa.di.scan.thesis.repository.PostRepository;
import gr.uoa.di.scan.thesis.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("tagService")
public class TagServiceImpl extends GenericServiceBase<Tag, TagDTO, Long> implements TagService {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private PostRepository postRepository;

	@Override
	JpaRepository<Tag, Long> getRepository() {
		return tagRepository;
	}

	@Override
	Class<Tag> getTypeofEntity() {
		return Tag.class;
	}

	@Override
	Class<TagDTO> getTypeofDTO() {
		return TagDTO.class;
	}

	@Override
	@Transactional(rollbackFor = EntityNotFoundException.class)
	public void delete(Long id) {
		Tag tag = tagRepository.findOne(id);
		if (tag == null)
			throw new EntityNotFoundException(Tag.class.getSimpleName() + " not found");
		for (Post post : tag.getPosts()) {
			post.getTags().remove(tag);
			postRepository.saveAndFlush(post);
		}
		tagRepository.delete(tag);
		tagRepository.flush();
	}

	@Transactional
	public TagDTO findByTitle(String title) {
		Tag tag = tagRepository.findByTitle(title);
		if(tag == null)
			return null;
		return mapper.map(tag, getTypeofDTO());
	}
	

}
