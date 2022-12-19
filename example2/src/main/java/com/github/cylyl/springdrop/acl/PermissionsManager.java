package com.github.cylyl.springdrop.acl;

import com.github.cylyl.springdrop.account.Account;
import com.github.cylyl.springdrop.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

/**
 * To use Spring Security ACL, we need to create four mandatory tables in our database.
 * The first table is ACL_CLASS, which store class name of the domain object, columns include:
 *
 * ID
 * CLASS: the class name of secured domain objects, for example: com.github.cylyl.springdrop.account.Account
 *
 * Secondly, we need the ACL_SID table which allows us to universally identify any principle or authority in the system. The table needs:
 *
 * ID
 * SID: which is the username or role name. SID stands for Security Identity
 * PRINCIPAL: 0 or 1, to indicate that the corresponding SID is a principal (user, such as mary, mike, jack…) or an authority (role, such as ROLE_ADMIN, ROLE_USER, ROLE_EDITOR…)
 *
 * Next table is ACL_OBJECT_IDENTITY, which stores information for each unique domain object:
 *
 * ID
 * OBJECT_ID_CLASS: define the domain object class, links to ACL_CLASS table
 * OBJECT_ID_IDENTITY: domain objects can be stored in many tables depending on the class. Hence, this field store the target object primary key
 * PARENT_OBJECT: specify parent of this Object Identity within this table
 * OWNER_SID: ID of the object owner, links to ACL_SID table
 * ENTRIES_INHERITING: whether ACL Entries of this object inherits from the parent object (ACL Entries are defined in ACL_ENTRY table)
 *
 * Finally, the ACL_ENTRY store individual permission assigns to each SID on an Object Identity:
 *
 * ID
 * ACL_OBJECT_IDENTITY: specify the object identity, links to ACL_OBJECT_IDENTITY table
 * ACE_ORDER: the order of current entry in the ACL entries list of corresponding Object Identity
 * SID: the target SID which the permission is granted to or denied from, links to ACL_SID table
 * MASK: the integer bit mask that represents the actual permission being granted or denied
 * GRANTING: value 1 means granting, value 0 means denying
 * AUDIT_SUCCESS and AUDIT_FAILURE: for auditing purpose
 */
@Service
public class PermissionsManager {

    @Value("${spring.security.user.name}")
    private String defaultAdminName;


    @Value("${spring.security.user.password}")
    private String defaultAdminPassword;

    @Autowired
    private JdbcMutableAclService aclService;

    private PasswordEncoder passwordEncoder;

    private JdbcUserDetailsManager userDetailsManager;

    @Autowired private TransactionTemplate tt;

    @Autowired private AccountService accountService;


    private void initUser() {
        createAdminUser(defaultAdminName, defaultAdminPassword);
    }
    private void creteAclInTransaction(final ObjectIdentity objectIdentity) {
        this.tt.execute((arg0) -> {
            this.aclService.createAcl(objectIdentity);
            return null;
        });
    }

    private void updateAclInTransaction(final MutableAcl acl) {
        this.tt.execute((arg0) -> {
            this.aclService.updateAcl(acl);
            return null;
        });
    }
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;

    }

    public void setUserDetailsManager(JdbcUserDetailsManager users) {
        this.userDetailsManager = users;
        if(defaultAdminName != null && defaultAdminPassword != null) {
            initUser();
        }
    }

    public void createUserAccount(String username, String password) {
        if(userExists(username) == false) {
            createUser(username, password);
            Account account =  accountService.save(username);
            changeOwner(account, account.getUsername());
        }
    }

    private void createUser(String username, String password) {
        createUser(username, password, "USER");
    }
    private void createAdminUser(String username, String password) {
        createUser(username, password, "ADMIN", "USER");
    }
    private void createUser(String username, String password, String ...role) {
        if(userExists(username)) {
            return;
        }
        UserDetails user = User.builder().username(username)
                .password(passwordEncoder.encode(password))
                .roles(role)
                .build();
        userDetailsManager.createUser(user);
    }

    public void grantPermissions(Class cls, long objectId, String username, Permission permission) {
        AclImpl acl;
        try { acl = (AclImpl) this.aclService
                .readAclById(new ObjectIdentityImpl(cls, objectId));
            acl.insertAce(acl.getEntries().size(), permission, new PrincipalSid(username), true);
            updateAclInTransaction(acl);
        }
        catch (Exception e) {
            if(e instanceof NotFoundException) {
                creteAclInTransaction(new ObjectIdentityImpl(cls,objectId));
                grantPermissions(cls, objectId, username, permission);
            }
        }
    }

    public void grantPermissions(Account account, Permission permission) {
        grantPermissions(
                account.getClass(), account.getId(), account.getUsername(), permission
        );
    }

    private void changeOwner(Class cls, long objectId, String newUsername) {

        AclImpl acl;
        try { acl = (AclImpl) this.aclService
                .readAclById(new ObjectIdentityImpl(cls, objectId));
            acl.setOwner(new PrincipalSid(newUsername));
            updateAclInTransaction(acl);
        }
        catch (Exception e) {
            if(e instanceof NotFoundException) {
                creteAclInTransaction(new ObjectIdentityImpl(cls,objectId));
                changeOwner(cls, objectId, newUsername);
            }
        }
    }

    public void changeOwner(Account account, String username) {
        changeOwner(account.getClass(), account.getId(), username);
    }

    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

    public List<String> findUsersInGroup(String groupName) {
        return userDetailsManager.findUsersInGroup(groupName);
    }

    public void createGroup(final String groupName, final List<GrantedAuthority> authorities) {
        userDetailsManager.createGroup(groupName, authorities);
    }

    public void deleteGroup(String groupName) {
        userDetailsManager.deleteGroup(groupName);
    }

    public void addUserToGroup(final String username, final String groupName) {
        userDetailsManager.addUserToGroup(username, groupName);
    }

    public void removeUserFromGroup(final String username, final String groupName) {
        userDetailsManager.removeUserFromGroup(username, groupName);
    }

    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        return userDetailsManager.findGroupAuthorities(groupName);
    }

    public void removeGroupAuthority(String groupName, final GrantedAuthority authority) {
        userDetailsManager.removeGroupAuthority(groupName, authority);
    }

    public void addGroupAuthority(final String groupName, final GrantedAuthority authority) {
        userDetailsManager.addGroupAuthority(groupName, authority);
    }


    public Optional<Account> findUserAccountById(int id) {
        return accountService.findById(id);
    }
}
