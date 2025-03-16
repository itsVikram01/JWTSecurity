package com.user.service;

import com.user.exception.DuplicateUsernameException;
import com.user.exception.UserAlreadyExistsException;
import com.user.exception.UserNotFoundException;
import com.user.model.User;
import com.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRoles("USER");
    }

    @Test
    void createUser_WhenUsernameDoesNotExist_ShouldSaveUserWithEncodedPassword() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void createUser_WhenUsernameExists_ShouldThrowUserAlreadyExistsException() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));
    }

    /*@Test
    void updateUser_WhenUserExistsAndNewUsernameNotTaken_ShouldUpdateUser() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("newPassword");
        newUser.setRoles("ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User updatedUser = userService.updateUser(1L, newUser);

        assertEquals("newUser", updatedUser.getUsername());
        assertEquals("encodedNewPassword", updatedUser.getPassword());
        assertEquals("ADMIN", updatedUser.getRoles());
        verify(userRepository, times(1)).save(user);
    }*/

   /* @Test
    void updateUser_WhenNewUsernameTakenByAnotherUser_ShouldThrowDuplicateUsernameException() {
        User newUser = new User();
        newUser.setUsername("existingUser");
        newUser.setRoles("USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> userService.updateUser(1L, newUser));
    }*/

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, new User()));
    }

    @Test
    void updateUser_WhenPasswordIsEmpty_ShouldNotEncodePassword() {
        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setPassword("");
        newUser.setRoles("USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(1L, newUser);

        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testUser");

        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("testUser");

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetailsWithAuthorities() {
        user.setRoles("USER,ADMIN");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testUser"));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
    }
}