package com.bca.byc.util;

import com.bca.byc.exception.InvalidFileTypeException;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadHelper {

    public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID() + fileExtension;
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    public static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath.replaceAll("/uploads", "src/main/resources/static/uploads"));
            if (Files.exists(path)) {
                Files.delete(path); // Delete the file if it exists
                System.out.println("File deleted: " + path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + filePath, e);
        }
    }

    public static void validateFileTypeImage(MultipartFile file) throws InvalidFileTypeException, IOException {
        // Create a Tika instance
        Tika tika = new Tika();

        // Detect the MIME type of the uploaded file
        String mimeType = tika.detect(file.getInputStream());

        System.out.println("Detected MIME type: " + mimeType);

        // Validate the MIME type
        if (!mimeType.startsWith("image/")) {
            throw new InvalidFileTypeException("Only image files are allowed.");
        }
    }


}
