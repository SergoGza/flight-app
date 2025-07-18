package com.tokioschool.flightapp.store.service;

import com.tokioschool.flightapp.store.dto.ResourceContentDTO;
import com.tokioschool.flightapp.store.dto.ResourceIdDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {

  Optional<ResourceIdDTO> saveResource(MultipartFile multipartFile, @Nullable String description);

  Optional<ResourceContentDTO> findResource(UUID resourceId);

  void deleteResource(UUID resourceId);
}
