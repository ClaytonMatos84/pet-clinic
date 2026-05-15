package pet.be.pet_clinic.owner;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pet.be.pet_clinic.owner.dto.PetRequest;
import pet.be.pet_clinic.owner.dto.PetResponse;
import pet.be.pet_clinic.owner.dto.VisitResponse;

@RestController
@RequestMapping("/api/owners/{ownerId}/pets")
@Tag(name = "Pets", description = "Pet management APIs")
public class PetController {

	private final OwnerRepository ownerRepository;
	private final PetRepository petRepository;
	private final PetTypeRepository petTypeRepository;

	public PetController(OwnerRepository ownerRepository, PetRepository petRepository,
			PetTypeRepository petTypeRepository) {
		this.ownerRepository = ownerRepository;
		this.petRepository = petRepository;
		this.petTypeRepository = petTypeRepository;
	}

	@GetMapping
	@Operation(summary = "List pets for an owner")
	public List<PetResponse> list(@PathVariable Integer ownerId) {
		if (!ownerRepository.existsById(ownerId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
		}
		return petRepository.findByOwnerId(ownerId).stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add a new pet to an owner")
	public PetResponse create(@PathVariable Integer ownerId, @Valid @RequestBody PetRequest request) {
		Owner owner = ownerRepository.findById(ownerId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
		PetType type = petTypeRepository.findById(request.typeId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pet type"));

		Pet pet = new Pet();
		pet.setName(request.name());
		pet.setBirthDate(request.birthDate());
		pet.setType(type);
		pet.setOwner(owner);
		pet = petRepository.save(pet);
		return toResponse(pet);
	}

	@PutMapping("/{petId}")
	@Operation(summary = "Update a pet")
	public PetResponse update(@PathVariable Integer ownerId, @PathVariable Integer petId,
			@Valid @RequestBody PetRequest request) {
		if (!ownerRepository.existsById(ownerId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
		}
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
		PetType type = petTypeRepository.findById(request.typeId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pet type"));

		pet.setName(request.name());
		pet.setBirthDate(request.birthDate());
		pet.setType(type);
		pet = petRepository.save(pet);
		return toResponse(pet);
	}

	private PetResponse toResponse(Pet pet) {
		return new PetResponse(
				pet.getId(),
				pet.getName(),
				pet.getBirthDate(),
				pet.getType() != null ? pet.getType().getName() : null,
				pet.getVisits().stream()
						.map(v -> new VisitResponse(v.getId(), v.getDate(), v.getDescription()))
						.collect(Collectors.toList()));
	}

}
