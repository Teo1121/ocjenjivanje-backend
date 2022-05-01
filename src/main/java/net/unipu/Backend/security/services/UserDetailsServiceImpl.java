package net.unipu.Backend.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.unipu.Backend.models.User;
import net.unipu.Backend.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
            .orElseGet(() -> userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username)));
    System.out.println(user.getUsername());
    return UserDetailsImpl.build(user);
  }

}
