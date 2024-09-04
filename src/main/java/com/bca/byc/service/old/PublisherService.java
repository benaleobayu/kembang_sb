package com.bca.byc.service.old;

import com.bca.byc.entity.old.Publisher;
import com.bca.byc.model.old.PublisherCreateRequestDTO;
import com.bca.byc.model.old.PublisherListResponseDTO;
import com.bca.byc.model.old.PublisherResponseDTO;
import com.bca.byc.model.old.PublisherUpdateRequestDTO;
import com.bca.byc.response.ResultPageResponseDTO;

public interface PublisherService {

	public void createPublisher(PublisherCreateRequestDTO dto);
	
	public Publisher findPublisher(String publisherId);

	public void updatePublisher(String publisherId, PublisherUpdateRequestDTO dto);

	public ResultPageResponseDTO<PublisherListResponseDTO> findPublisherList(Integer pages, Integer limit,
			String sortBy, String direction, String publisherName);
	
	public PublisherResponseDTO constructDTO(Publisher publisher);

}
