package gr.uoa.di.scan.thesis.repository;

import gr.uoa.di.scan.thesis.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
