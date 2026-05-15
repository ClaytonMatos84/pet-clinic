package pet.be.pet_clinic.owner;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pet.be.pet_clinic.owner.dto.VisitRequest;
import pet.be.pet_clinic.owner.dto.VisitResponse;

@RestController
@RequestMapping("/api/owners/{ownerId}/pets/{petId}/visits")
@Tag(name = "Visits", description = "Visit management APIs")
public class VisitController {

	private final OwnerRepository ownerRepository;
	private final PetRepository petRepository;
	private final VisitRepository visitRepository;

	public VisitController(OwnerRepository ownerRepository, PetRepository petRepository,
			VisitRepository visitRepository) {
		this.ownerRepository = ownerRepository;
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
	}

	@GetMapping
	@Operation(summary = "List visits for a pet")
	public List<VisitResponse> list(@PathVariable Integer ownerId, @PathVariable Integer petId) {
		if (!ownerRepository.existsById(ownerId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
		}
		if (!petRepository.existsById(petId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
		}
		return visitRepository.findByPetId(petId).stream()
				.map(v -> new VisitResponse(v.getId(), v.getDate(), v.getDescription()))
				.collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add a visit to a pet")
	public VisitResponse create(@PathVariable Integer ownerId, @PathVariable Integer petId,
			@Valid @RequestBody VisitRequest request) {
		if (!ownerRepository.existsById(ownerId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
		}
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));

		Visit visit = new Visit();
		visit.setDate(request.date());
		visit.setDescription(request.description());
		visit.setPet(pet);
		visit = visitRepository.save(visit);
		return new VisitResponse(visit.getId(), visit.getDate(), visit.getDescription());
	}

}
