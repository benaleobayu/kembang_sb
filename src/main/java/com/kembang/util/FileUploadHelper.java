package com.kembang.util;

import com.kembang.exception.InvalidFileTypeImageException;
import com.kembang.exception.InvalidFileTypeImageVideoException;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
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

    public static void validateFileImageSvg(MultipartFile file) throws InvalidFileTypeImageException, IOException {
        String filename = file.getOriginalFilename();
        List<String> validExtensions = List.of("svg");

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

        throw new InvalidFileTypeImageVideoException("Only image with SVG files are allowed.");

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


    public static void downloadFileFromUrl(String url, String originalFilename) throws IOException {
        String fileName = originalFilename;
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
