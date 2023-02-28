package com.github.cylyl.springdrop.account;

import com.github.cylyl.springdrop.acl.PermissionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/account")
public class AccountController {


    @Autowired
    AccountService accountService;

    @Autowired
    PermissionsManager permissionsManager;

    @GetMapping
    public String get() {

        return "hello from spring";
    }

    public void createUserAccount(String username, String password) {
        permissionsManager.createUserAccount(username, password);
    }

    public void createGroup(String group) {
        createGroup(group, Arrays.asList(
                (GrantedAuthority) () -> "write"
        ));
    }
    public void createGroup(String group, List<GrantedAuthority> authorities) {
        permissionsManager.createGroup(group, authorities);
    }

    public void addUserToGroup(String username, String group) {
        permissionsManager.addUserToGroup(username, group);
    }


    public Optional<Account> findUserAccountById(Integer id) {
        Optional<Account> accOpt = permissionsManager.findUserAccountById(id);
        return accOpt;
    }

}
