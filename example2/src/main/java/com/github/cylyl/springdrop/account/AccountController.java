package com.github.cylyl.springdrop.account;

import com.github.cylyl.springdrop.acl.PermissionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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

        permissionsManager.createUserAccount("user1", "user1");
        permissionsManager.createUserAccount("user2", "user2");
        Optional<Account> accOpt = permissionsManager.findUserAccountById(1);
        Optional<Account> acc2Opt = permissionsManager.findUserAccountById(2);

        permissionsManager.createGroup("grp1", Arrays.asList(
                (GrantedAuthority) () -> "write"
        ));
        permissionsManager.createGroup("grp2", Arrays.asList(
                (GrantedAuthority) () -> "write"
        ));
        permissionsManager.addUserToGroup(accOpt.get().getUsername(), "grp1");
        permissionsManager.addUserToGroup(acc2Opt.get().getUsername(), "grp2");

        Optional<Account> adminOpt = permissionsManager.findUserAccountById(0);
        return "hello from spring";
    }

}
