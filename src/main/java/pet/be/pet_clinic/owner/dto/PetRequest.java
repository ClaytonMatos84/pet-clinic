package pet.be.pet_clinic.owner.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record PetRequest(
		@NotBlank String name,
		@NotNull @PastOrPresent LocalDate birthDate,
		@NotNull Integer typeId
) {

}
