package com.bca.byc.controller.old;

import java.net.URI;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bca.byc.annotation.LogThisMethod;
import com.bca.byc.model.old.PublisherCreateRequestDTO;
import com.bca.byc.model.old.PublisherListResponseDTO;
import com.bca.byc.model.old.PublisherUpdateRequestDTO;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.service.old.PublisherService;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@Validated
@AllArgsConstructor
@RestController
public class PublisherResource {

	private final PublisherService publisherService;

	@Operation(hidden = true)
	@PreAuthorize("hasAuthority('admin.create')")
	@PostMapping("/v1/publisher")
	public ResponseEntity<Void> createNewPublisher(@RequestBody @Valid PublisherCreateRequestDTO dto) {
		publisherService.createPublisher(dto);
		return ResponseEntity.created(URI.create("/v1/publisher")).build();
	}

	@Operation(hidden = true)
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/v1/publisher/{publisherId}")
	public ResponseEntity<Void> updatePublisher(@PathVariable 
			@Size(max = 36, min = 36, message = "publiher.id.not.uuid") String publisherId,
			@RequestBody @Valid PublisherUpdateRequestDTO dto) {
		publisherService.updatePublisher(publisherId, dto);
		return ResponseEntity.ok().build();
	}

//	@Operation(hidden = true)
	@PreAuthorize("isAuthenticated()")
	@LogThisMethod
	@GetMapping("/v1/publisher")
	public ResponseEntity<ResultPageResponseDTO<PublisherListResponseDTO>> findPublisherList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages, 
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name="sortBy", required = true, defaultValue = "name") String sortBy,
			@RequestParam(name="direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name="publisherName", required = false) String publisherName){
		if(pages<0) throw new BadRequestException("invalid pages");
		return ResponseEntity.ok().body(publisherService.findPublisherList(pages, limit, sortBy, direction, publisherName));
	}

}
