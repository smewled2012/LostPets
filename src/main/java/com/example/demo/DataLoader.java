package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PetsRepository petsRepository;


  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... strings) throws Exception {

      boolean rundataloader = false;


      if (rundataloader) {
          roleRepository.save(new Role("USER"));
          roleRepository.save(new Role("ADMIN"));

          Role adminRole = roleRepository.findByRole("ADMIN");
          Role userRole = roleRepository.findByRole("USER");

          User user = new User("jim@jim.com", passwordEncoder.encode("password"), "Jim", "Jimmerson", true,
                  "jim");
          user.setRoles(Arrays.asList(userRole));
          userRepository.save(user);

          user = new User("admin@admin.com", passwordEncoder.encode("password"),
                  "Admin",
                  "User", true,
                  "admin");
          user.setRoles(Arrays.asList(adminRole));
          userRepository.save(user);

          Pets pet=new Pets("kitty","2018-10-10","soft and nice","https://res.cloudinary.com/djgbgmhqz/image/upload/v1541600785/yfnr2rt0f0r79azswhyh.jpg","lost");
          petsRepository.save(pet);

      }
  }

}
