package com.bca.byc.repository.old;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bca.byc.entity.old.Author;
import com.bca.byc.model.old.AuthorQueryDTO;

public interface AuthorRepository extends JpaRepository<Author, Long>{

	//method name convention
	//find+keyword
	//sql -> select * from Author a where a.id= :authorId
	public Optional<Author> findById(Long id);
	
	public List<Author> findBySecureIdIn(List<String> authorIdList);
		
	public Optional<Author> findBySecureId(String id);
	//where id = :id AND deleted=false
	public Optional<Author> findByIdAndIsDeletedFalse(Long id);

	
	//sql -> select a from Author a where a.author_name = :authorName
	public List<Author> findByNameLike(String authorName);
	
	@Query("SELECT new com.bca.byc.model.old.AuthorQueryDTO(b.id, ba.name) "
			+ "FROM Book b "
			+ "JOIN b.authors ba "
			+ "WHERE b.id IN :bookIdList")
	public List<AuthorQueryDTO> findAuthorsByBookIdList(List<Long> bookIdList); 
	
}
