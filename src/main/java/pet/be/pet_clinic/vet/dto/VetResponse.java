package pet.be.pet_clinic.vet.dto;

import java.util.Set;

public record VetResponse(
		Integer id,
		String firstName,
		String lastName,
		Set<String> specialties
) {

}
