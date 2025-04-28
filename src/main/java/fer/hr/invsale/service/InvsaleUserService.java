package fer.hr.invsale.service;

import fer.hr.invsale.DAO.InvsaleUser;
import fer.hr.invsale.DAO.Role;
import fer.hr.invsale.DTO.invsaleUser.CreateInvsaleUserDTO;
import fer.hr.invsale.DTO.invsaleUser.InvsaleUserDTO;
import fer.hr.invsale.DTO.invsaleUser.UpdateInvsaleUserDTO;
import fer.hr.invsale.repository.InvsaleUserRepository;
import fer.hr.invsale.repository.RoleRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;

@Service
public class InvsaleUserService implements UserDetailsService{

    @Autowired
    InvsaleUserRepository invsaleUserRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<InvsaleUserDTO> getAllUsers() {
        return invsaleUserRepository.findAll().stream().map(InvsaleUserDTO::toDto).toList();
    }

    public Optional<InvsaleUserDTO> getUserByEmail(String email) {
        return invsaleUserRepository.findById(email).map(InvsaleUserDTO::toDto);
    }

    public InvsaleUserDTO createUser(CreateInvsaleUserDTO user) throws IllegalArgumentException{
        if(invsaleUserRepository.existsById(user.getEmail()))
            throw new KeyAlreadyExistsException("Email " + user.getEmail() + " already exists.");
        return InvsaleUserDTO.toDto(
                invsaleUserRepository.save(createUserFromDto(user)));
    }

    private InvsaleUser createUserFromDto(CreateInvsaleUserDTO user) throws IllegalArgumentException{
        InvsaleUser createdUser = new InvsaleUser(user.getEmail());
        Role role = roleRepository.findById("USER")
                .orElseThrow(() -> new IllegalArgumentException("Role does not exist."));
        createdUser.setRole(role);
        Optional.ofNullable(user.getPhoneNumber()).ifPresent(createdUser::setPhoneNumber);
        Optional.ofNullable(user.getPassword()).ifPresent(createdUser::setHashedPassword);
        return createdUser;
    }

    public void updateUser(UpdateInvsaleUserDTO user) throws NoSuchObjectException {
        if(!invsaleUserRepository.existsById(user.getEmail()))
            throw new NoSuchObjectException("User \"" + user.getEmail() + "\" does not exist.");

        invsaleUserRepository.save(updateFromDto(user));
    }

    private InvsaleUser updateFromDto(UpdateInvsaleUserDTO user) {
        InvsaleUser updatedUser = invsaleUserRepository.findById(user.getEmail())
                .orElseThrow(() -> new NullPointerException("User does not exist."));
        Optional.ofNullable(user.getPassword()).ifPresent(
                password -> updatedUser.setHashedPassword(String.valueOf(password.hashCode())));
        Optional.ofNullable(user.getPhoneNumber()).ifPresent(updatedUser::setPhoneNumber);
        return updatedUser;
    }

    public void deleteUser(String email) throws NoSuchObjectException {
        if(!invsaleUserRepository.existsById(email))
            throw new NoSuchObjectException("User \"" + email + "\" does not exist.");

        invsaleUserRepository.deleteById(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var invsaleUser = invsaleUserRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(invsaleUser.getEmail())
                .password(invsaleUser.getHashedPassword())
                .roles(invsaleUser.getRole().getName())
                .build();
    }

    public @NonNull Boolean isAdmin(@NonNull String email) {
        return invsaleUserRepository.findById(email).get()
                .getRole()
                .getName()
                .equalsIgnoreCase("admin");
    }
}
