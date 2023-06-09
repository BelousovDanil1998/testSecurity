package Belousov.Spring.SpringSecurity.services;

import Belousov.Spring.SpringSecurity.Model.Role;
import Belousov.Spring.SpringSecurity.Model.User;
import Belousov.Spring.SpringSecurity.repositories.RoleRepository;
import Belousov.Spring.SpringSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private RoleRepository roleRepo;

    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepo;

    public void registerDefaultUser(User user) {
        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);
        encodePassword(user);
        userRepo.save(user);
    }

    @Autowired
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }


    public List<User> listAll() {
        return userRepo.findAll();
    }

    public User get(Long id) {
        return userRepo.findById(id).get();
    }

    public List<Role> listRoles() {
        return roleRepo.findAll();
    }

    public void save(User user) {
        userRepo.save(user);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
