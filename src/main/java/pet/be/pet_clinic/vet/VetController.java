package pet.be.pet_clinic.vet;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import pet.be.pet_clinic.vet.dto.VetResponse;

@RestController
@RequestMapping("/api/vets")
@Tag(name = "Veterinarians", description = "Veterinarian directory")
public class VetController {

	private final VetRepository vetRepository;

	public VetController(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@GetMapping
	@Operation(summary = "List veterinarians")
	public Page<VetResponse> list(@ParameterObject Pageable pageable) {
		return vetRepository.findAll(pageable).map(this::toResponse);
	}

	private VetResponse toResponse(Vet vet) {
		return new VetResponse(
				vet.getId(),
				vet.getFirstName(),
				vet.getLastName(),
				vet.getSpecialties().stream()
						.map(Specialty::getName)
						.collect(Collectors.toSet()));
	}

}
