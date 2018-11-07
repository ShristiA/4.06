package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public Long countByEmail(String email){
        return userRepository.countByEmail(email);
    }

    public User findByUsername(String username){
        return userRepository.findByUserName(username);
    }

    public void saveUser(User user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setEnabled(true);
        userRepository.save(user);
    }
    public void saveAdmin(User user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("ADMIN")));
        user.setEnabled(true);
        userRepository.save(user);
    }
    public class SSUserDetailsService implements UserDetailsService {
        private UserRepository userRepository;
        public SSUserDetailsService(UserRepository userRepository){
            this.userRepository=userRepository;
        }
        @Override
        public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
            try{
                User user = userRepository.findByUserName(username);
                if(user==null) {
                    return null;
                }
                return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),getAuthorities(user));
                }catch (Exception e){
                throw new UsernameNotFoundException("User not found");
            }
        }
        private Set<GrantedAuthority> getAuthorities(User user) {
            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            for(Role role: user.getRoles()){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
                authorities.add(grantedAuthority);
            }
            return authorities;
        }

    }
}
