package com.sample.awsrds.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private double price;
}
