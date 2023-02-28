package com.github.cylyl.springdrop.account;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Entity
public class Account {

    public Account() {

    }

    public Account(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private String username;
}
