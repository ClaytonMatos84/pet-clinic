package pet.be.pet_clinic.owner.dto;

import java.time.LocalDate;
import java.util.List;

public record PetResponse(
		Integer id,
		String name,
		LocalDate birthDate,
		String type,
		List<VisitResponse> visits
) {

}
