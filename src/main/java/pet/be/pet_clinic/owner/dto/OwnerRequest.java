package pet.be.pet_clinic.owner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OwnerRequest(
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotBlank String address,
		@NotBlank String city,
		@NotBlank String telephone,
		@NotBlank @Email String email
) {

}
