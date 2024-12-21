package com.kembang.service.impl;

import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.AppUser;
import com.kembang.repository.CustomerRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.service.ImportService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final AppAdminRepository adminRepository;
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void CustomerImport(MultipartFile file) throws IOException {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);
        List<AppUser> datas = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // Baca header untuk mendapatkan mapping indeks kolom ke nama header
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> headerMap = new HashMap<>();
        for (Cell cell : headerRow) {
            headerMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
        }

        // Validasi bahwa semua header yang diperlukan tersedia
        String[] requiredHeaders = {
                "name", "phone", "address", "location", "day_subscribed", "is_subscribed", "is_active"
        };
        for (String header : requiredHeaders) {
            if (!headerMap.containsKey(header)) {
                throw new IllegalArgumentException("Header " + header + " is missing in the file");
            }
        }

        // Iterasi baris mulai dari baris ke-1 (data, bukan header)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            AppUser data = new AppUser();
            data.setName(getCellValueAsString(row.getCell(headerMap.get("name"))));
            data.setPhone(getCellValueAsString(row.getCell(headerMap.get("phone"))));
            data.setAddress(getCellValueAsString(row.getCell(headerMap.get("address"))));
            data.setLocation(getCellValueAsString(row.getCell(headerMap.get("location"))));
            data.setDaySubscribed(getCellValueAsString(row.getCell(headerMap.get("day_subscribed"))));
            data.setIsSubscribed(Boolean.parseBoolean(getCellValueAsString(row.getCell(headerMap.get("is_subscribed")))));
            data.setIsActive(Boolean.parseBoolean(getCellValueAsString(row.getCell(headerMap.get("is_active")))));

            String prefixEmail = data.getName()
                    .replaceAll("[^a-zA-Z0-9]+", "_")
                    .replaceAll("\\s", "_")
                    .toLowerCase();
            data.setEmail(prefixEmail + "@apps.net");
            data.setPassword(passwordEncoder.encode("password"));
            data.setCreatedAt(LocalDateTime.now());
            data.setCreatedBy(adminLogin.getId());
            data.setUpdatedAt(LocalDateTime.now());

            datas.add(data);
        }

        workbook.close();
        customerRepository.saveAll(datas);
    }

    /**
     * Helper method to get cell value as String regardless of its type.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Handle date values
                    return cell.getDateCellValue().toString();
                } else {
                    // Handle numeric values
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                throw new IllegalArgumentException("Unsupported cell type: " + cell.getCellType());
        }
    }

}
