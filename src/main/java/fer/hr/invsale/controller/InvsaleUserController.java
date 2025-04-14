package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.invsaleUser.CreateInvsaleUserDTO;
import fer.hr.invsale.DTO.invsaleUser.InvsaleUserDTO;
import fer.hr.invsale.DTO.invsaleUser.UpdateInvsaleUserDTO;
import fer.hr.invsale.service.InvsaleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing users in the Invsale system.
 * <p>
 * This controller handles user-related operations such as:
 * retrieving all users, retrieving a specific user by email,
 * creating a new user, updating an existing user, and deleting a user.
 * </p>
 *
 * <p>
 * All responses are returned as {@link ResponseEntity},
 * and the data transfer objects used include {@code InvsaleUserDTO},
 * {@code CreateInvsaleUserDTO}, and {@code UpdateInvsaleUserDTO}.
 * </p>
 * Example endpoints:
 * <ul>
 *     <li><b>GET</b> /api/users/ – Retrieves all users</li>
 *     <li><b>GET</b> /api/users/{email} – Retrieves user by email</li>
 *     <li><b>POST</b> /api/users/ – Creates a new user</li>
 *     <li><b>PUT</b> /api/users/ – Updates an existing user</li>
 *     <li><b>DELETE</b> /api/users/{email} – Deletes a user by email</li>
 * </ul>
 *
 * @author Dorotea Dragojević
 * @version 1.0
 */
@RestController
@RequestMapping("/api/users")
public class InvsaleUserController {

    @Autowired
    InvsaleUserService invsaleUserService;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of {@link InvsaleUserDTO} wrapped in {@link ResponseEntity}
     */
    @GetMapping("/")
    public ResponseEntity<List<InvsaleUserDTO>> getAllUsers() {
        return ResponseEntity.ok(invsaleUserService.getAllUsers());
    }

    /**
     * Retrieves a specific user based on their email address.
     *
     * @param email the email of the user to retrieve
     * @return the user as {@link InvsaleUserDTO} if found, otherwise 404 Not Found
     */
    @GetMapping("/{email}")
    public ResponseEntity<InvsaleUserDTO> getUserByEmail(@PathVariable String email) {
        return invsaleUserService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     *
     * @param user the user data to be created
     * @return the created {@link InvsaleUserDTO} with status 201 Created,
     *         409 Conflict if user already exists, or 400 Bad Request if given role does not exist
     */
    @PostMapping("/")
    public ResponseEntity<InvsaleUserDTO> createUser(@RequestBody CreateInvsaleUserDTO user) {
        try{
            return new ResponseEntity<>(invsaleUserService.createUser(user), HttpStatus.CREATED);
        }catch(KeyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user the updated user data
     * @return 204 No Content if update was successful,
     *         or 404 Not Found if the user does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateInvsaleUserDTO user) {
        try{
            invsaleUserService.updateUser(user);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a user by email.
     *
     * @param email the email of the user to delete
     * @return 204 No Content if deletion was successful,
     *         or 404 Not Found if the user does not exist
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        try{
            invsaleUserService.deleteUser(email);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // metode get liked products by user za product controller
    // metode like i remove u controller
}
