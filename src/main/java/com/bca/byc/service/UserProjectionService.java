package com.bca.byc.service;

import java.util.List;
import java.util.Map;

public interface UserProjectionService {

    Map<Long, String> findUserNameMaps(List<Long> reportedIdList);

    Map<Long, String> findUserPhoneMaps(List<Long> reportedIdList);

}
