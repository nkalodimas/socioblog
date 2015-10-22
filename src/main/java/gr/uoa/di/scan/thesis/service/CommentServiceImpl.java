package gr.uoa.di.scan.thesis.service;

import gr.uoa.di.scan.thesis.dto.CommentDTO;
import gr.uoa.di.scan.thesis.entity.Comment;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;
import gr.uoa.di.scan.thesis.repository.CommentRepository;
import gr.uoa.di.scan.thesis.repository.PostRepository;
import gr.uoa.di.scan.thesis.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("commentService")
public class CommentServiceImpl extends GenericServiceBase<Comment, CommentDTO, Long> implements CommentService{

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	JpaRepository<Comment, Long> getRepository() {
		
		return commentRepository;
	}

	@Override
	Class<Comment> getTypeofEntity() {
		
		return Comment.class;
	}

	@Override
	Class<CommentDTO> getTypeofDTO() {
		
		return CommentDTO.class;
	}

	@Override
	@Transactional(rollbackFor = EntityNotFoundException.class)
	public CommentDTO create(CommentDTO dto) throws EntityNotFoundException {
		
		if (!userRepository.exists(dto.getPostedBy().getId()))
			throw new EntityNotFoundException(getTypeofEntity().getSimpleName() + " not found");
		
		if (dto.getPostedInComment() != null && !commentRepository.exists(dto.getPostedInComment().getId()))
			throw new EntityNotFoundException(getTypeofEntity().getSimpleName() + " not found");
		
		if (dto.getPostedInPost() != null && !postRepository.exists(dto.getPostedInPost().getId()))
			throw new EntityNotFoundException(getTypeofEntity().getSimpleName() + " not found");
		
		return super.create(dto);
	}

}
