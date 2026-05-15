package pet.be.pet_clinic.owner.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VisitRequest(
		@NotNull
		@Future(message = "data da visita deve ser futura")
		LocalDate date,
		@NotBlank String description
) {

}
