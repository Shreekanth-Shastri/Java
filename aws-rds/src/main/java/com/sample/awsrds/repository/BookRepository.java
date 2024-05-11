package com.sample.awsrds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.awsrds.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
