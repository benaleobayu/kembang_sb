package com.bca.byc.controller.old;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bca.byc.model.old.AuthorCreateRequestDTO;
import com.bca.byc.model.old.AuthorResponseDTO;
import com.bca.byc.model.old.AuthorUpdateRequestDTO;
import com.bca.byc.service.old.AuthorService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Validated
public class AuthorResource {
	
	private final AuthorService authorService;
	
	//author detail
	@Operation(hidden = true)
	@GetMapping("/v1/author/{id}/detail")
	public ResponseEntity<AuthorResponseDTO> findAuthorById(@PathVariable String id){
		return ResponseEntity.ok().body(authorService.findAuthorById(id));
	}

	@Operation(hidden = true)
	@PreAuthorize("hasAuthority('admin.create')")
	@PostMapping("/v1/author")
	public ResponseEntity<Void> createNewAuthor(@RequestBody @Valid List<AuthorCreateRequestDTO> dto){
		authorService.createNewAuthor(dto);
		return ResponseEntity.created(URI.create("/author")).build();
	}


	@Operation(hidden = true)
	@PutMapping("/v1/author/{authorId}")
	public ResponseEntity<Void> updateAuthor(@PathVariable String authorId, 
		 @RequestBody AuthorUpdateRequestDTO dto){
		authorService.updateAuthor(authorId, dto);
		return ResponseEntity.ok().build();
	}

	@Operation(hidden = true)
	@DeleteMapping("/v1/author/{authorId}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable String authorId){
		authorService.deleteAuthor(authorId);
		return ResponseEntity.ok().build();
	}
	

}
