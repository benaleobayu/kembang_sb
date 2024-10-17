package com.bca.byc.util;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.exception.InvalidFileTypeImageVideoException;
import com.bca.byc.model.attribute.PostContentRequest;
import com.bca.byc.model.projection.IdSecureIdProjection;
import com.bca.byc.repository.auth.AppUserRepository;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public static void deleteFile(String filePath, String UPLOAD_DIR) {
        try {
            Path path = Paths.get(filePath.replaceAll("/uploads", UPLOAD_DIR));
            if (Files.exists(path)) {
                Files.delete(path); // Delete the file if it exists
                System.out.println("File deleted: " + path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + filePath, e);
        }
    }

    public static void validateFileTypeImage(MultipartFile file) throws InvalidFileTypeImageException, IOException {
        String filename = file.getOriginalFilename();
        List<String> validExtensions = List.of("jpg", "jpeg", "png", "heic");

        if (filename != null && !isValidExtension(filename, validExtensions)) {
            throw new InvalidFileTypeImageVideoException("Only " + validExtensions + " files are allowed.");
        }

        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());

        System.out.println("Detected MIME type: " + mimeType);

        // Validate the MIME type for images
        if (mimeType.startsWith("image/") || mimeType.equals("video/mp4")) {
            return;
        }

        throw new InvalidFileTypeImageVideoException("Only image and MP4 files are allowed.");

    }

    public static void validateFileTypePost(MultipartFile file) throws InvalidFileTypeImageException, IOException {
        String filename = file.getOriginalFilename();
        List<String> validExtensions = List.of("jpg", "jpeg", "png", "heic", "mp4");

        if (filename != null && !isValidExtension(filename, validExtensions)) {
            throw new InvalidFileTypeImageVideoException("Only " + validExtensions + " files are allowed.");
        }

        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());

        System.out.println("Detected MIME type: " + mimeType);

        // Validate the MIME type for images
        if (mimeType.startsWith("image/") || mimeType.equals("video/mp4")) {
            return;
        }

        throw new InvalidFileTypeImageVideoException("Only image and MP4 files are allowed.");
    }

    // mime validations
    private static boolean isValidExtension(String filename, List<String> validExtensions) {
        if (filename == null) {
            return false;
        }
        String lowerCaseName = filename.toLowerCase();
        return validExtensions.stream().anyMatch(lowerCaseName::endsWith);
    }

    // Method to check if the file is a video
    public static boolean isVideoFile(MultipartFile file) {
        Tika tika = new Tika();

        try {
            String mimeType = tika.detect(file.getInputStream());
            System.out.println("Detected MIME type: " + mimeType);

            return mimeType.startsWith("video/");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method to convert video to M3U8
    public static String convertVideoToM3U8(String videoPath, String UPLOAD_DIR, String VIDEO_PATH) throws IOException {
        String outputDir = UPLOAD_DIR + VIDEO_PATH;
        String m3u8FileName = UUID.randomUUID() + ".m3u8";
        String command = String.format("ffmpeg -i %s -codec: copy -start_number 0 -hls_time 10 -hls_list_size 0 -f hls %s/%s",
                videoPath, outputDir, m3u8FileName);

        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.inheritIO();
        Process process = processBuilder.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Video conversion interrupted", e);
        }

        return outputDir + m3u8FileName; // Return the path to the M3U8 file
    }

    public static PostContent processFile(MultipartFile file, MultipartFile thumbnail, PostContentRequest contentRequest, int index, String UPLOAD_DIR, AppUserRepository userRepository) throws IOException {
        String filePath = saveFile(file, UPLOAD_DIR + "/post/");
        String contentType = file.getContentType();
        String fileType = null;


        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
//                    || fileName.endsWith(".gif") || fileName.endsWith(".bmp") ||
//                    fileName.endsWith(".tiff") || fileName.endsWith(".webp") ||
//                    fileName.endsWith(".svg") || fileName.endsWith(".ico")
            ) {
                contentType = "image/webp"; // Default to WEBP
                fileType = "image";
            } else if (fileName.endsWith(".mp4")
//                    || fileName.endsWith(".avi") ||
//                    fileName.endsWith(".mkv") || fileName.endsWith(".mov") ||
//                    fileName.endsWith(".wmv") || fileName.endsWith(".flv") ||
//                    fileName.endsWith(".mpeg") || fileName.endsWith(".3gp")
            ) {
                contentType = "video/mp4"; // Default to MP4
                fileType = "video";
            } else {
                throw new RuntimeException("Unsupported file type: " + fileName);
            }
        }


        // Membuat PostContent dari file dan contentRequest yang sesuai
        PostContent postContent = new PostContent();
        postContent.setIndex(index);
        postContent.setContent(filePath.replaceAll("src/main/resources/static/", "/"));
        postContent.setType(fileType);
        postContent.setOriginalName(contentRequest.getOriginalName());

        if (thumbnail != null) {
            String thumbnailPath = saveFile(thumbnail, UPLOAD_DIR + "/thumbnail/");
            postContent.setThumbnail(thumbnailPath.replaceAll("src/main/resources/static/", "/"));
        }

        // Menangani tagUserIds
        Set<AppUser> appUsers = new HashSet<>();
        if (contentRequest.getTagUserIds() != null) {
            for (String userId : contentRequest.getTagUserIds()) {
                try {
                    UUID.fromString(userId); // Validate UUID
                    IdSecureIdProjection gotId = userRepository.findUserBySecureId(userId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                    appUsers.add(gotId.toAppUser());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid UUID format: " + userId);
                }

            }
        }
        postContent.setTagUsers(appUsers);

        return postContent;
    }

}
