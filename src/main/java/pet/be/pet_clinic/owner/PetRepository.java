package pet.be.pet_clinic.owner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {

	List<Pet> findByOwnerId(Integer ownerId);

}
