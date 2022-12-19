package com.github.cylyl.springdrop.example.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> list() {
        return bookRepository.findAll();
    }
}