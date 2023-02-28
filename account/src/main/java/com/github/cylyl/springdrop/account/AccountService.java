package com.github.cylyl.springdrop.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired AccountRepository accountRepository;

//    @PreAuthorize("hasPermission(#account, 'READ')")
//    @PreAuthorize("hasPermission(#account, 'WRITE')")

//    @PostFilter("hasPermission(filterObject, 'READ')")
//    List<Account> findAll();
//
//    @PostAuthorize("hasPermission(returnObject, 'READ')")
//    Account findById(Integer id);
//
//    @PreAuthorize("hasPermission(#account, 'WRITE')")
//    Account save(@Param("account")Account account);

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account save(String username) {
        return accountRepository.save(new Account.AccountBuilder().username(username).build());
    }

    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }
}
