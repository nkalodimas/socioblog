package gr.uoa.di.scan.thesis.repository;

import gr.uoa.di.scan.thesis.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>{

	public Tag findByTitle(String title);
}
