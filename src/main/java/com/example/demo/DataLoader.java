package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings)throws Exception{
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");

        User user = new User("jim@jim.com", passwordEncoder.encode("password"),"JIm","Jimmerson",true,"Jim");
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        user = new User("admin@admin.com", passwordEncoder.encode("password"),"Admin","User",true,"admin");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);
    }
}
