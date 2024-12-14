package com.kembang.converter.parsing;

import com.kembang.entity.AppAdmin;
import com.kembang.entity.Location;
import com.kembang.entity.OrderRoute;
import com.kembang.repository.LocationRepository;
import com.kembang.repository.OrderRouteRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;

public class TreeGetEntityProjection {

    public static AppAdmin getParsingAdminByProjection(String secureId, AppAdminRepository repository){
        return HandlerRepository.getIdBySecureId(
                secureId,
                repository::findIdBySecureId,
                projection -> repository.findById(projection.getId()),
                "Admin not found"
        );
    }

    public static Location getParsingLocationByProjection(String secureId, LocationRepository repository){
        return HandlerRepository.getIdBySecureId(
                secureId,
                repository::findIdBySecureId,
                projection -> repository.findById(projection.getId()),
                "Location not found"
        );
    }

    public static OrderRoute getParsingOrderRouteByProjection(String secureId, OrderRouteRepository repository){
        return HandlerRepository.getIdBySecureId(
                secureId,
                repository::findIdBySecureId,
                projection -> repository.findById(projection.getId()),
                "Route not found"
        );
    }



}
