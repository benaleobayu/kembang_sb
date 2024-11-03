package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailResponseDTO implements Serializable {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 2141744514662455235L;
	private String username;
}
