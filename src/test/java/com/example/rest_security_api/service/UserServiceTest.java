package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.dto.UserUpdateDto;
import com.example.rest_security_api.dto.UserUpdatePasswordDto;
import com.example.rest_security_api.entity.Role;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.entity.User;
import com.example.rest_security_api.mapper.UserCreateMapper;
import com.example.rest_security_api.mapper.UserReadMapper;
import com.example.rest_security_api.mapper.UserUpdateMapper;
import com.example.rest_security_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserReadMapper userReadMapper;
    @Mock
    private UserCreateMapper userCreateMapper;
    @Mock
    private UserUpdateMapper userUpdateMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void findAll() {
        User timur = User.builder()
                .username("Тимур")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        UserReadDto timurDto = UserReadDto
                .builder()
                .username("Тимур")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();

        User artem = User.builder()
                .username("Артем")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        UserReadDto artemDto = UserReadDto
                .builder()
                .username("Артем")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();

        ArrayList<User> listUserReadDto = new ArrayList<>();
        listUserReadDto.add(timur);
        listUserReadDto.add(artem);

        doReturn(listUserReadDto).when(userRepository).findAll();
        doReturn(timurDto).when(userReadMapper).mapToDto(timur);
        doReturn(artemDto).when(userReadMapper).mapToDto(artem);

        List<UserReadDto> actual = userService.findAll();

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(listUserReadDto.size());

    }

    @Test
    void getById() {
        Integer userid = 1;
        User timur = User.builder()
                .id(1)
                .username("Timur")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        Optional<User> optionalTimur = Optional.of(timur);
        UserReadDto timurReadDto = UserReadDto.builder()
                .id(1)
                .username("Timur")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        Optional<UserReadDto> optionalUserReadDto = Optional.of(timurReadDto);


        doReturn(optionalTimur).when(userRepository).findById(userid);
        doReturn(timurReadDto).when(userReadMapper).mapToDto(timur);

        Optional<UserReadDto> actual = userService.getById(userid);

        assertThat(actual).isPresent();
        assertThat(actual).isEqualTo(optionalUserReadDto);
    }

    @Test
    void create() {
        UserCreateDto timurCreateDto = UserCreateDto.builder()
                .username("Timur")
                .rawPassword("123")
                .build();
        User user = User.builder()
                .username("Timur")
                .password("encode password")
                .build();
        UserReadDto timurReadDto = UserReadDto.builder()
                .status(Status.ACTIVE)
                .role(Role.USER)
                .username("Timur")
                .build();

        doReturn(user).when(userCreateMapper).mapToEntity(timurCreateDto);
        doReturn(user).when(userRepository).save(user);
        doReturn(timurReadDto).when(userReadMapper).mapToDto(user);

        UserReadDto actual = userService.create(timurCreateDto);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(timurReadDto);
    }

    @Test
    void updateOwnUser() {
        Integer changeId = 1;
        User initialUser = User.builder()
                .id(1)
                .username("Timur")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        Optional<User> optionalInitialUser = Optional.of(initialUser);

        UserUpdateDto userUpdateDto = new UserUpdateDto("Евгений");

        User updateUser = User.builder()
                .id(1)
                .username("Евгений")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();

        UserReadDto userReadDto = UserReadDto.builder()
                .id(1)
                .username("Евгений")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();


        doReturn(optionalInitialUser).when(userRepository).findByUsername("Timur");
        doReturn(updateUser).when(userUpdateMapper).copy(userUpdateDto, initialUser);
        doReturn(updateUser).when(userRepository).save(updateUser);
        doReturn(userReadDto).when(userReadMapper).mapToDto(updateUser);

        UserReadDto actual = userService.updateOwnUser(userUpdateDto, changeId, "Timur");

        assertThat(actual).isEqualTo(userReadDto);
        assertThat(actual).isNotNull();
    }

    @Test
    void updateById() {
        Integer changeId = 1;
        User initialUser = User.builder()
                .username("Timur")
                .id(1)
                .status(Status.ACTIVE)
                .role(Role.USER)
                .build();
        UserUpdateDto userUpdateDto = new UserUpdateDto("Евегний");

        User savedUser = User.builder()
                .username("Евгений")
                .id(1)
                .status(Status.ACTIVE)
                .role(Role.USER)
                .build();
        UserReadDto savedUserReadDto = UserReadDto.builder()
                .id(1)
                .username("Евгений")
                .status(Status.ACTIVE)
                .role(Role.USER)
                .build();


        doReturn(initialUser).when(userRepository).getById(changeId);
        doReturn(savedUser).when(userUpdateMapper).copy(userUpdateDto, initialUser);
        doReturn(savedUser).when(userRepository).save(savedUser);
        doReturn(savedUserReadDto).when(userReadMapper).mapToDto(savedUser);

        UserReadDto actual = userService.updateById(changeId, userUpdateDto);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(savedUserReadDto);
    }

    @Test
    void getByUsername() {
        String username = "Timur";
        Optional<User> optionalUser = Optional.of(User.builder()
                .id(1)
                .username("Timur")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build());
        User user = optionalUser.get();

        doReturn(optionalUser).when(userRepository).findByUsername(username);

        User actual = userService.getByUsername(username);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(user);
    }

    @Test
    void loadUserByUsername() {
        String username = "Timur";
        User user = User.builder()
                .id(1)
                .username("Timur")
                .password("Encode password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        Optional<User> optionalUser = Optional.of(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().getAuthority()).build();

        doReturn(optionalUser).when(userRepository).findByUsername(username);

        UserDetails actual = userService.loadUserByUsername(username);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(userDetails);
    }

    @Test
    void deleteOwnUser() {
        String authUsername = "Timur";
        Integer deleteUserId = 1;
        User deleteableUser = User.builder()
                .id(1)
                .username("Timur")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        Optional<User> optionalUser = Optional.of(deleteableUser);


        doReturn(optionalUser).when(userRepository).findById(deleteUserId);

        userService.deleteOwnUser(authUsername, deleteUserId);


        verify(userRepository, times(1)).delete(deleteableUser);


    }

    @Test
    void deleteById() {
        Integer deleteUserId = 1;
        User deleteableUser = User.builder()
                .id(1)
                .username("Timur")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        Optional<User> optionalDeleteableUser = Optional.of(deleteableUser);

        doReturn(optionalDeleteableUser).when(userRepository).findById(deleteUserId);

        userService.deleteById(deleteUserId);

        verify(userRepository, times(1)).delete(deleteableUser);
    }

    @Test
    void updatePassword() {
        Integer id = 1;
        String newPassword = "encode new password";
        String authUsername = "Timur";
        UserUpdatePasswordDto userUpdatePasswordDto = new UserUpdatePasswordDto("decode_new_password");
        User updateUser = User.builder()
                .id(1)
                .username("Timur")
                .status(Status.ACTIVE)
                .role(Role.USER)
                .password("encode_old_password")
                .build();
        Optional<User> optionalUpdatePasswordUser1 = Optional.of(updateUser);

        doReturn(optionalUpdatePasswordUser1).when(userRepository).findById(id);
        doReturn(newPassword).when(passwordEncoder).encode(userUpdatePasswordDto.getRawPassword());

        userService.updatePassword(id, userUpdatePasswordDto, authUsername);
    }
}