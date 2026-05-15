package pet.be.pet_clinic.owner.dto;

import java.util.List;

public record OwnerResponse(
		Integer id,
		String firstName,
		String lastName,
		String address,
		String city,
		String telephone,
		String email,
		List<PetResponse> pets
) {

}
