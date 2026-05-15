package pet.be.pet_clinic.owner;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import pet.be.pet_clinic.owner.dto.OwnerRequest;
import pet.be.pet_clinic.owner.dto.OwnerResponse;
import pet.be.pet_clinic.owner.dto.PetResponse;
import pet.be.pet_clinic.owner.dto.VisitResponse;

@RestController
@RequestMapping("/api/owners")
@Tag(name = "Owners", description = "Owner management APIs")
public class OwnerController {

	private final OwnerRepository ownerRepository;

	public OwnerController(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@GetMapping
	@Operation(summary = "Search and list owners")
	public Page<OwnerResponse> list(
			@RequestParam(defaultValue = "") String name,
			@ParameterObject Pageable pageable) {
		Page<Owner> page;
		if (name.isBlank()) {
			page = ownerRepository.findAll(pageable);
		}
		else {
			page = ownerRepository.findByName(name, pageable);
		}
		return page.map(this::toResponse);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get owner by ID")
	public OwnerResponse getById(@PathVariable Integer id) {
		Owner owner = ownerRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
		return toResponse(owner);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create new owner")
	public OwnerResponse create(@Valid @RequestBody OwnerRequest request) {
		Owner owner = new Owner();
		applyRequest(owner, request);
		owner = ownerRepository.save(owner);
		return toResponse(owner);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update owner")
	public OwnerResponse update(@PathVariable Integer id, @Valid @RequestBody OwnerRequest request) {
		Owner owner = ownerRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
		applyRequest(owner, request);
		owner = ownerRepository.save(owner);
		return toResponse(owner);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete owner")
	public void delete(@PathVariable Integer id) {
		if (!ownerRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
		}
		ownerRepository.deleteById(id);
	}

	private void applyRequest(Owner owner, OwnerRequest request) {
		owner.setFirstName(request.firstName());
		owner.setLastName(request.lastName());
		owner.setAddress(request.address());
		owner.setCity(request.city());
		owner.setTelephone(request.telephone());
		owner.setEmail(request.email());
	}

	private OwnerResponse toResponse(Owner owner) {
		List<PetResponse> pets = owner.getPets().stream()
				.map(pet -> new PetResponse(
						pet.getId(),
						pet.getName(),
						pet.getBirthDate(),
						pet.getType() != null ? pet.getType().getName() : null,
						pet.getVisits().stream()
								.map(v -> new VisitResponse(v.getId(), v.getDate(), v.getDescription()))
								.collect(Collectors.toList())))
				.collect(Collectors.toList());
		return new OwnerResponse(
				owner.getId(),
				owner.getFirstName(),
				owner.getLastName(),
				owner.getAddress(),
				owner.getCity(),
				owner.getTelephone(),
				owner.getEmail(),
				pets);
	}

}
