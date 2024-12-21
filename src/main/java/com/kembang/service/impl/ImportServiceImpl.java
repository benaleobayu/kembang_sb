package com.kembang.service.impl;

import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.AppUser;
import com.kembang.repository.CustomerRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.service.ImportService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final AppAdminRepository adminRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void CustomerImport(MultipartFile file) throws IOException {
            AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);
            List<AppUser> datas = new ArrayList<>();

            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                AppUser data = new AppUser();
                data.setName(row.getCell(0).getStringCellValue());

                data.setIsActive(row.getCell(3).getBooleanCellValue());

                data.setCreatedAt(LocalDateTime.now());
                data.setCreatedBy(adminLogin.getId());
                data.setUpdatedAt(LocalDateTime.now());

                datas.add(data);
            }
            workbook.close();
        customerRepository.saveAll(datas);
    }
}
