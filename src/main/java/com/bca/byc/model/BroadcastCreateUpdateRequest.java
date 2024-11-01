package com.bca.byc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BroadcastCreateUpdateRequest {

    private MultipartFile file;

    private String title;

    private String message;

    @Schema(example = "DRAFT", description = "DRAFT | SENT | SCHEDULED")
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postAt;

}
