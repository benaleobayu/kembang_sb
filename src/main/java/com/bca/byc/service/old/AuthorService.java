package com.bca.byc.service.old;

import java.util.List;
import java.util.Map;

import com.bca.byc.entity.old.Author;
import com.bca.byc.model.old.AuthorCreateRequestDTO;
import com.bca.byc.model.old.AuthorResponseDTO;
import com.bca.byc.model.old.AuthorUpdateRequestDTO;

public interface AuthorService {
	
	public AuthorResponseDTO findAuthorById(String id);
	
	public void createNewAuthor(List<AuthorCreateRequestDTO> dto);
	
	public void updateAuthor(String authorId, AuthorUpdateRequestDTO dto);
	
	public void deleteAuthor(String authorId);
	
	public List<Author> findAuthors(List<String> authorIdList);
	
	public List<AuthorResponseDTO> constructDTO(List<Author> authors);
	
	public Map<Long, List<String>> findAuthorMaps(List<Long> authorIdList);

}
