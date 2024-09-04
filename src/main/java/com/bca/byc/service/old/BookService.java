package com.bca.byc.service.old;

import java.util.List;

import com.bca.byc.model.old.BookCreateRequestDTO;
import com.bca.byc.model.old.BookDetailResponseDTO;
import com.bca.byc.model.old.BookListResponseDTO;
import com.bca.byc.model.old.BookUpdateRequestDTO;
import com.bca.byc.response.ResultPageResponseDTO;

public interface BookService {
	
	public BookDetailResponseDTO findBookDetailById(String bookId);
	
	public List<BookDetailResponseDTO> findBookListDetail();
	
	public void createNewBook(BookCreateRequestDTO dto);

	public void updateBook(Long bookId, BookUpdateRequestDTO dto);

	public void deleteBook(Long bookId);

	public ResultPageResponseDTO<BookListResponseDTO> findBookList(Integer page, Integer limit, String sortBy,
			String direction, String publisherName, String bookTitle, String authorName);

}
