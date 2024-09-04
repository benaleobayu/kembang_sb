package com.bca.byc.controller.old;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bca.byc.model.UserDetailResponseDTO;
import com.bca.byc.service.AppUserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserResource {

	private final AppUserService appUserService;

	@Operation(hidden = true)
	@GetMapping("/v1/user")
	public ResponseEntity<UserDetailResponseDTO> findUserDetail(){
		return ResponseEntity.ok(appUserService.findUserDetail());
		
	}
}
