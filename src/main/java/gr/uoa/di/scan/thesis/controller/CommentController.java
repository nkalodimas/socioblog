package gr.uoa.di.scan.thesis.controller;

import gr.uoa.di.scan.thesis.dto.CommentDTO;
import gr.uoa.di.scan.thesis.entity.Comment;
import gr.uoa.di.scan.thesis.service.CommentService;
import gr.uoa.di.scan.thesis.service.GenericService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentController extends GenericController<Comment,CommentDTO, Long>{
	
	@Autowired
	CommentService commentService;
	
	@Override
	GenericService<Comment,CommentDTO, Long> getService() {
		
		return commentService;
	}
	
}
