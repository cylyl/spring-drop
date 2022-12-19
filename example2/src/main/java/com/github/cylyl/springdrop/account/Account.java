package com.github.cylyl.springdrop.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

@Builder
@Entity
public class Account {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private String username;
}
