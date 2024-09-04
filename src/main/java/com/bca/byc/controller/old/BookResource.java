package com.bca.byc.controller.old;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bca.byc.model.old.BookCreateRequestDTO;
import com.bca.byc.model.old.BookDetailResponseDTO;
import com.bca.byc.model.old.BookListResponseDTO;
import com.bca.byc.model.old.BookUpdateRequestDTO;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.old.BookService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class BookResource {
	
	private final BookService bookService;

	//nama yang salah /get-book/{bookId}
	@Operation(hidden = true)
	@PreAuthorize("hasAuthority('admin.read')")
	@GetMapping("/v1/book/{bookId}")
	public ResponseEntity<BookDetailResponseDTO> findBookDetail(@PathVariable("bookId") String id) {
		StopWatch stopWatch = new StopWatch();
		log.info("start findBookDetail "+id);
		stopWatch.start();
		BookDetailResponseDTO result =  bookService.findBookDetailById(id);
		stopWatch.stop();
		log.info("finish findBookDetail. execution time = {}",stopWatch.getTotalTimeMillis());
		return ResponseEntity.ok(result);

	}
	
	//nama yang salah /save-book /create-book
	@Operation(hidden = true)
	@PostMapping("/v1/book")
	public ResponseEntity<Void> createANewBook(@RequestBody BookCreateRequestDTO dto){
		bookService.createNewBook(dto);
		return ResponseEntity.created(URI.create("/book")).build();
	}
	
	
	//get boot list ->
	//1. judul buku
	//2. nama penerbit //table publisher
	//3. nama penulis //table author

	@Operation(hidden = true)
	@GetMapping("/v2/book")
	public ResponseEntity<ResultPageResponseDTO<BookListResponseDTO>> findBookList(
			@RequestParam(name = "page", required = true, defaultValue = "0") Integer page, 
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit, 
			@RequestParam(name = "sortBy", required = true, defaultValue = "title") String sortBy,
			@RequestParam(name = "direction",required = true, defaultValue = "asc") String direction,
			@RequestParam(name = "bookTitle",required = false, defaultValue = "") String bookTitle,
			@RequestParam(name = "publisherName",required = false, defaultValue = "") String publisherName,
			@RequestParam(name = "authorName",required = false, defaultValue = "") String authorName){
		return ResponseEntity.ok().body(bookService.findBookList(page, limit, sortBy, direction, publisherName, bookTitle, authorName));
		
	}


	@Operation(hidden = true)
	@GetMapping("/v1/book")
	public ResponseEntity<List<BookDetailResponseDTO>> findBookList(){
		return ResponseEntity.ok().body(bookService.findBookListDetail());
		
	}
	
	//PUT /book
	@Operation(hidden = true)
	@PutMapping("/v1/book/{bookId}")
	public ResponseEntity<Void> updateBook(@PathVariable("bookId") Long bookId, 
			@RequestBody BookUpdateRequestDTO dto){
		bookService.updateBook(bookId, dto);
		return ResponseEntity.ok().build();
	}
	
	//DELETE /book
	@Operation(hidden = true)
	@DeleteMapping("/v1/book/{bookId}")
	public ResponseEntity<Void> deleteBook(@PathVariable("bookId") Long bookId){
		bookService.deleteBook(bookId);
		return ResponseEntity.ok().build();
	}
	
}