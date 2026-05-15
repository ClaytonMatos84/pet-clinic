package pet.be.pet_clinic.owner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OwnerRequest(
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotBlank String address,
		@NotBlank String city,
		@NotBlank
		@Pattern(regexp = "^\\d{10,11}$", message = "telefone deve conter apenas 10 ou 11 digitos")
		String telephone,
		@NotBlank @Email String email
) {

}
