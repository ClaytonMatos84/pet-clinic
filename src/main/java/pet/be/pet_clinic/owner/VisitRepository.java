package pet.be.pet_clinic.owner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

	List<Visit> findByPetId(Integer petId);

}
