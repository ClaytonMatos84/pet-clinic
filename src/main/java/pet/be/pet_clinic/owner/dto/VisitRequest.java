package pet.be.pet_clinic.owner.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VisitRequest(
		@NotNull LocalDate date,
		@NotBlank String description
) {

}
