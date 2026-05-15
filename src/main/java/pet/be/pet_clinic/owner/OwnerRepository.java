package pet.be.pet_clinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

	@Query("SELECT o FROM Owner o WHERE LOWER(o.firstName) LIKE LOWER(CONCAT('%', :name, '%')) "
			+ "OR LOWER(o.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page<Owner> findByName(@Param("name") String name, Pageable pageable);

}
