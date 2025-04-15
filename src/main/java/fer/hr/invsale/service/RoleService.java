package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Role;
import fer.hr.invsale.DTO.role.RoleDTO;
import fer.hr.invsale.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(
                RoleDTO::toDto
        ).toList();
    }

    public RoleDTO createRole(RoleDTO role) {
        if (roleRepository.existsById(role.getName()))
            throw new KeyAlreadyExistsException("Role " + role.getName() + " already exists.");

        return RoleDTO.toDto(roleRepository.save(new Role(role.getName())));
    }

    public void deleteUser(RoleDTO role) throws NoSuchObjectException {
        if(!roleRepository.existsById(role.getName()))
            throw new NoSuchObjectException("Role " + role.getName() + " does not exist.");
        roleRepository.deleteById(role.getName());
    }
}
