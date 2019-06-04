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

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserDeviceService userDeviceService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleService roleService, UserDeviceService userDeviceService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userDeviceService = userDeviceService;
    }

    /**
     * 유저이름으로 사용자 조회
     * Finds a user in the database by username
     */
    public Mono<Optional<User>> findByUsername(String username) {
        
    	return Mono.fromCallable(() -> userRepository.findByUsername(username))
    			.subscribeOn(Schedulers.elastic());
    }

    /**
     * 이메일로 사용자 조회
     */
    public Mono<Optional<User>> findByEmail(String email) {
    	
    	return Mono.fromCallable(() -> userRepository.findByEmail(email))
    			.subscribeOn(Schedulers.elastic());
    }

    /**
     * id값으로 유저 조회
     * Find a user in db by id.
     */
    public Mono<Optional<User>> findById(Long Id) {
    	return Mono.fromCallable(() -> userRepository.findById(Id))
    			.subscribeOn(Schedulers.elastic());
    }

    /**
     * Save the user to the database
     */
    public Mono<User> save(User registeredUser) {
    	return Mono.fromCallable(() -> userRepository.save(registeredUser))
		.subscribeOn(Schedulers.elastic());
    }

    /**
     * Check is the user exists given the email: naturalId
     */
    public Mono<Boolean> existsByEmail(String email) {
        
    	return Mono.fromCallable(() -> userRepository.existsByEmail(email))
    			.subscribeOn(Schedulers.elastic());
    }

    /**
     * Check is the user exists given the username: naturalId
     */
    public Mono<Boolean> existsByUsername(String username) {
    	
    	return Mono.fromCallable(() -> userRepository.existsByUsername(username))
    			.subscribeOn(Schedulers.elastic());
    }


    /**
     * Creates a new user from the registration request
     */
    public Mono<User> createUser(RegistrationRequest registerRequest) {
    	
    	return Mono.fromCallable(() -> {
    		User newUser = new User();
            Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setUsername(registerRequest.getEmail());
            newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin).block());
            newUser.setActive(true);
            newUser.setEmailVerified(false);
            return newUser;
    		
    	})
		.subscribeOn(Schedulers.elastic());
    }

    /**
     * Performs a quick check to see what roles the new user could be assigned to.
     *
     * @return list of roles for the new user
     */
    private Mono<Set<Role>> getRolesForNewUser(Boolean isToBeMadeAdmin) {
    	
    	return Mono.fromCallable(() -> {
    		Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
            if (!isToBeMadeAdmin) {
                newUserRoles.removeIf(Role::isAdminRole);
            }
            logger.info("Setting user roles: " + newUserRoles);
            return newUserRoles;
    		
    	})
		.subscribeOn(Schedulers.elastic());
    }

    /**
     * Log the given user out and delete the refresh token associated with it. If no device
     * id is found matching the database for the given user, throw a log out exception.
     */
    public Mono<Void> logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutRequest logOutRequest) {
        
    	// 리턴값이 있는 경우 fromCallable,
    	// 리턴값이 없는 경우 fromCallable ... then()
    	
    	return Mono.fromRunnable(() -> {
            String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
            UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
                    .filter(device -> device.getDeviceId().equals(deviceId))
                    .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user "));

            logger.info("Removing refresh token associated with device [" + userDevice + "]");
            
    	}).subscribeOn(Schedulers.elastic()).then();
    }
}
