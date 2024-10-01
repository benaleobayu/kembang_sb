package com.bca.byc.service;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCatalog;
import com.bca.byc.repository.BusinessCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
@Service
public class BusinessCatalogService {

    @Autowired
    private BusinessCatalogRepository businessCatalogRepository;

    public BusinessCatalog saveCatalog(BusinessCatalog businessCatalog) {
        return businessCatalogRepository.save(businessCatalog);
    }
    public BusinessCatalog getCatalogBySecureId(String secureId) {
        return businessCatalogRepository.findBySecureId(secureId).orElse(null);
    }
    

    public BusinessCatalog getCatalogById(Long id) {
        Optional<BusinessCatalog> catalog = businessCatalogRepository.findById(id);
        return catalog.orElse(null);
    }

    public List<BusinessCatalog> getAllCatalogs() {
        return businessCatalogRepository.findAll();
    }

    public BusinessCatalog updateCatalog(String secureId, BusinessCatalog catalogDetails) {
        Optional<BusinessCatalog> catalogOptional = businessCatalogRepository.findBySecureId(secureId);
    
        if (catalogOptional.isPresent()) {
            BusinessCatalog catalog = catalogOptional.get();
            catalog.setTitle(catalogDetails.getTitle());
            catalog.setImage(catalogDetails.getImage());
            catalog.setDescription(catalogDetails.getDescription());
            return businessCatalogRepository.save(catalog);
        } else {
            return null;
        }
    }
    
    public boolean deleteCatalogBySecureId(String secureId) {
        Optional<BusinessCatalog> catalog = businessCatalogRepository.findBySecureId(secureId);
        if (catalog.isPresent()) {
            businessCatalogRepository.delete(catalog.get());
            return true;
        }
        return false;
    }
      public Page<BusinessCatalog> getCatalogsByBusiness(Business business, Pageable pageable) {
        return businessCatalogRepository.findByBusiness(business, pageable);
    }


    
}
