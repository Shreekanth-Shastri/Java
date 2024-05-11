package com.sample.awsrds.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.awsrds.entity.Book;
import com.sample.awsrds.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private BookRepository bookRepository;

    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book findBook(@PathVariable int id) throws Exception {
        return bookRepository.findById(id).orElseThrow(() -> new Exception("Book not available"));
    }

}
