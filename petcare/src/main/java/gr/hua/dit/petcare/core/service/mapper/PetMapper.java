package gr.hua.dit.petcare.core.service.mapper;

import gr.hua.dit.petcare.core.model.Pet;
import gr.hua.dit.petcare.core.service.model.PetView;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    
    private final UserMapper userMapper;

    public PetMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PetView toView(Pet pet) {
        if (pet == null) return null;

        return new PetView(
            pet.getId(),
            pet.getName(),
            pet.getSpecies(),
            pet.getBreed(),
            pet.getAge(),
            userMapper.toView(pet.getOwner())
        );
    }
}