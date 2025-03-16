package com.user.service;

import com.user.exception.*;
import com.user.model.User;
import com.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserService.class, BCryptPasswordEncoder.class})
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setUsername("integrationUser");
        testUser.setPassword("testPassword");
        testUser.setRoles("USER");
        userRepository.save(testUser);
    }

    @Test
    void createUser_WithNewUsername_ShouldPersistToDatabase() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("password");
        newUser.setRoles("ADMIN");

        User savedUser = userService.createUser(newUser);

        assertNotNull(savedUser.getId());
        assertTrue(passwordEncoder.matches("password", savedUser.getPassword()));
        assertEquals("ADMIN", savedUser.getRoles());
    }

    @Test
    void createUser_WithExistingUsername_ShouldThrowException() {
        User duplicateUser = new User();
        duplicateUser.setUsername("integrationUser");
        duplicateUser.setPassword("password");

        assertThrows(UserAlreadyExistsException.class, () ->
                userService.createUser(duplicateUser)
        );
    }

    @Test
    void updateUser_WithValidId_ShouldUpdateFields() {
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setRoles("ADMIN,MANAGER");

        User result = userService.updateUser(testUser.getId(), updatedUser);

        assertEquals("updatedUser", result.getUsername());
        assertTrue(passwordEncoder.matches("newPassword", result.getPassword()));
        assertEquals("ADMIN,MANAGER", result.getRoles());
    }

    @Test
    void deleteUser_WithValidId_ShouldRemoveFromDatabase() {
        userService.deleteUser(testUser.getId());

        Optional<User> deletedUser = userRepository.findById(testUser.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    void loadUserByUsername_WithValidUser_ShouldReturnUserDetails() {
        UserDetails userDetails = userService.loadUserByUsername("integrationUser");

        assertEquals("integrationUser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void getUserById_WithInvalidId_ShouldReturnEmpty() {
        Optional<User> user = userService.getUserById(999L);
        assertTrue(user.isEmpty());
    }

    /*@Test
    void getAllUsers_ShouldReturnAllRecords() {
        User secondUser = new User();
        secondUser.setUsername("secondUser");
        secondUser.setPassword("password");
        userRepository.save(secondUser);

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }*/

    /*@Test
    void updateUser_WithSameUsername_ShouldSucceed() {
        User updatedUser = new User();
        updatedUser.setUsername("integrationUser"); // Same username
        updatedUser.setPassword("newPassword");
        updatedUser.setRoles("USER");

        User result = userService.updateUser(testUser.getId(), updatedUser);

        assertEquals("integrationUser", result.getUsername());
        assertTrue(passwordEncoder.matches("newPassword", result.getPassword()));
    }*/

    @Test
    void loadUserByUsername_WithMultipleRoles_ShouldReturnAllAuthorities() {
        User adminUser = new User();
        adminUser.setUsername("adminUser");
        adminUser.setPassword("password");
        adminUser.setRoles("ADMIN,MANAGER");
        userRepository.save(adminUser);

        UserDetails userDetails = userService.loadUserByUsername("adminUser");

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MANAGER")));
    }
}