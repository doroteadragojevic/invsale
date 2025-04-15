package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.role.RoleDTO;
import fer.hr.invsale.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing user roles.
 * <p>
 * Provides endpoints to retrieve all roles, create a new role, and delete an existing role.
 * This controller interacts with the {@link RoleService} to perform business logic.
 * </p>
 *
 * <p>
 * Base URL: <code>/api/roles</code>
 * </p>
 *
 * <ul>
 *     <li><b>GET</b> /api/roles/ – Retrieve all roles</li>
 *     <li><b>POST</b> /api/roles/ – Create a new role</li>
 *     <li><b>DELETE</b> /api/roles/ – Delete an existing role</li>
 * </ul>
 *
 * @author Dorotea Dragojević
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * Retrieves a list of all available roles.
     *
     * @return a list of {@link RoleDTO} objects wrapped in {@link ResponseEntity} with HTTP status 200 (OK)
     */
    @GetMapping("/")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    /**
     * Creates a new role.
     *
     * @param role the {@link RoleDTO} containing role name to create
     * @return the created {@link RoleDTO} with HTTP status 201 (Created) if successful,
     *         or HTTP status 409 (Conflict) if the role already exists
     */
    @PostMapping("/")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO role) {
        try{
            return new ResponseEntity<>(roleService.createRole(role), HttpStatus.CREATED);
        }catch(KeyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Deletes an existing role.
     *
     * @param role the {@link RoleDTO} representing the role to delete
     * @return HTTP status 204 (No Content) if deletion is successful,
     *         or HTTP status 404 (Not Found) if the role does not exist
     */
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteRole(@RequestBody RoleDTO role) {
        try{
            roleService.deleteUser(role);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
