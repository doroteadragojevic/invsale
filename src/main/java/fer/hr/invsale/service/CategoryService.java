package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Category;
import fer.hr.invsale.DTO.category.CategoryDTO;
import fer.hr.invsale.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryDTO::toDto).toList();
    }

    public Optional<CategoryDTO> getCategoryById(String nameCategory) {
        return categoryRepository.findById(nameCategory).map(CategoryDTO::toDto);
    }

    public CategoryDTO createCategory(CategoryDTO category) {
        if(categoryRepository.existsById(category.getName()))
            throw new KeyAlreadyExistsException("Key " + category.getName() + " already exists.");

        return CategoryDTO.toDto(
                categoryRepository.save(new Category(category.getName(), category.getDescription())));
    }

    public void updateCategory(CategoryDTO category) throws NoSuchObjectException {
        if(!categoryRepository.existsById(category.getName()))
            throw new NoSuchObjectException("Category \"" + category.getName() + "\" does not exist.");

        categoryRepository.save(new Category(category.getName(), category.getDescription()));

    }

    public void deleteCategory(String name) throws NoSuchObjectException {
        if(!categoryRepository.existsById(name))
            throw new NoSuchObjectException("Category \"" + name + "\" does not exist.");

        categoryRepository.deleteById(name);

    }
}
