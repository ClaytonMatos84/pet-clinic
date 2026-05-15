package pet.be.pet_clinic.owner.dto;

import java.time.LocalDate;

public record VisitResponse(
		Integer id,
		LocalDate date,
		String description
) {

}
