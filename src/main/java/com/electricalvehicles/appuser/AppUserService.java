package com.electricalvehicles.appuser;

import com.electricalvehicles.appuser.request.CreateAppUserRequest;
import com.electricalvehicles.appuser.request.UpdateAppUserRequest;
import com.electricalvehicles.exception.DuplicateResourceException;
import com.electricalvehicles.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppUserService implements UserDetailsService {
    private final String USER_NOT_FOUND_MSG = "User with email %s not found.";
    private final AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public List<AppUserDto> getAllUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return users.stream().map(AppUserMapper::map)
                .collect(Collectors.toList());
    }

    public AppUserDto getUserById(final Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id [%s] not found".formatted(userId))
                );
        return AppUserMapper.map(user);
    }

    public Long createUser(final CreateAppUserRequest createRequest) {
        final boolean userExists = appUserRepository.existsByEmail(createRequest.email());
        if(userExists){
            throw new DuplicateResourceException("Email already taken");
        }

        AppUser user = AppUserMapper.map(createRequest);
        user = appUserRepository.save(user);

        return user.getId();
    }

    public void updateUser(final Long userId, final UpdateAppUserRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id [%s] not found".formatted(userId))
                );
        //TODO: wonder if password can be changed
        user.setFirstName(updateRequest.firstName());
        user.setLastName(updateRequest.lastName());

        if (updateRequest.email() != null && !updateRequest.email().equals(user.getEmail())) {
            if (appUserRepository.existsByEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "Email already taken."
                );
            }
            user.setEmail(updateRequest.email());
        }

        appUserRepository.save(user);
    }

    public void deleteUser(final Long userId) {
        appUserRepository.deleteById(userId);
    }
}
