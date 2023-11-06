package com.example.NewProject.services;

import com.example.NewProject.entities.Roles;
import com.example.NewProject.entities.Users;
import com.example.NewProject.repos.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService{

    private final UsersRepo usersRepo;

    private BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(8);
    }

    public Users findByUsername(String username){
        return usersRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = findByUsername(username);
        if (user == null){
            new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username));
        }

        return new User(
          user.getUsername(),
          user.getPassword(),
          user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList())
        );
    }

    public void registration(String username, String password, String nickname, String email){

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder().encode(password));
        user.setNickname(nickname);
        user.setEmail(email);
        user.setActive(true);
        if(username.equals("XXX")){
            user.setRoles(Collections.singleton(Roles.ROLE_ADMIN));
        }else{
            user.setRoles(Collections.singleton(Roles.ROLE_USER));
        }
        usersRepo.save(user);

    }

    public Users findUserByPrincipal(Principal principal){

        return usersRepo.findByUsername(principal.getName());

    }

    public String findNameById(long userId){

        String result = "";
        Optional<Users> users = usersRepo.findById(userId);
        List<Users> usersList = new ArrayList<>();
        users.ifPresent(usersList::add);

        for (var el : usersList){
            result = el.getNickname();
        }

        return result;
    }

    public Users findUserById(long userId){

        Users result = new Users();
        Optional<Users> users = usersRepo.findById(userId);
        List<Users> usersList = new ArrayList<>();
        users.ifPresent(usersList::add);

        for (var el : usersList){
            result = el;
        }
        return result;
    }


}
