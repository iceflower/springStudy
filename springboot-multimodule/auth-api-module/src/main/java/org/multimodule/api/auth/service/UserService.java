package org.multimodule.api.auth.service;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Logger;
import org.multimodule.common.annotation.CurrentUser;
import org.multimodule.common.entity.CustomUserDetails;
import org.multimodule.common.entity.Role;
import org.multimodule.common.entity.User;
import org.multimodule.common.entity.UserDevice;
import org.multimodule.common.entity.payload.LogOutRequest;
import org.multimodule.common.entity.payload.RegistrationRequest;
import org.multimodule.common.exception.UserLogoutException;
import org.multimodule.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserDeviceService userDeviceService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleService roleService, UserDeviceService userDeviceService, RefreshTokenService refreshTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userDeviceService = userDeviceService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Finds a user in the database by username
     */
    public Optional<User> findByUsername(String username) {
        return Optional.of(userRepository.findByUsername(username));
    }

    /**
     * Finds a user in the database by email
     */
    public Optional<User> findByEmail(String email) {
        return Optional.of(userRepository.findByEmail(email));
    }

    /**
     * Find a user in db by id.
     */
    public Optional<User> findById(Long Id) {
        return userRepository.findById(Id);
    }

    /**
     * Save the user to the database
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Check is the user exists given the email: naturalId
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Check is the user exists given the username: naturalId
     */
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    /**
     * Creates a new user from the registration request
     */
    public User createUser(RegistrationRequest registerRequest) {
        User newUser = new User();
        Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setUsername(registerRequest.getEmail());
        newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
        newUser.setActive(true);
        newUser.setEmailVerified(false);
        return newUser;
    }

    /**
     * Performs a quick check to see what roles the new user could be assigned to.
     *
     * @return list of roles for the new user
     */
    private Set<Role> getRolesForNewUser(Boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin) {
            newUserRoles.removeIf(Role::isAdminRole);
        }
        logger.info("Setting user roles: " + newUserRoles);
        return newUserRoles;
    }

    /**
     * Log the given user out and delete the refresh token associated with it. If no device
     * id is found matching the database for the given user, throw a log out exception.
     */
    public void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutRequest logOutRequest) {
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user "));

        logger.info("Removing refresh token associated with device [" + userDevice + "]");
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
    }
}
