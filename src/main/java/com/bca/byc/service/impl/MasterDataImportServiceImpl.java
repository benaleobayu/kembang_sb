package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.BlacklistKeyword;
import com.bca.byc.repository.BlacklistKeywordRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.service.MasterDataImportService;
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
public class MasterDataImportServiceImpl implements MasterDataImportService {

    private final AppAdminRepository adminRepository;

    private final BlacklistKeywordRepository blacklistKeywordRepository;

    @Override
    public void importBlacklistKeyword(MultipartFile file) throws IOException {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);
        List<BlacklistKeyword> blacklistKeywords = new ArrayList<>();


        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            BlacklistKeyword data = new BlacklistKeyword();
            data.setKeyword(row.getCell(0).getStringCellValue());
            data.setIsActive(row.getCell(1).getBooleanCellValue());

            data.setCreatedAt(LocalDateTime.now());
            data.setCreatedBy(adminLogin);
            data.setUpdatedAt(LocalDateTime.now());

            blacklistKeywords.add(data);
        }
        workbook.close();
        blacklistKeywordRepository.saveAll(blacklistKeywords);


    }
}
