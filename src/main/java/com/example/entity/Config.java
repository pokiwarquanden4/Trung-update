package com.example.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "config")
public class Config {
    @Id
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
}
