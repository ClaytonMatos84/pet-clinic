package pet.be.pet_clinic.owner;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pettypes")
@Tag(name = "Pet Types", description = "Pet type APIs")
public class PetTypeController {

	private final PetTypeRepository petTypeRepository;

	public PetTypeController(PetTypeRepository petTypeRepository) {
		this.petTypeRepository = petTypeRepository;
	}

	@GetMapping
	@Operation(summary = "List all pet types")
	public List<PetType> list() {
		return petTypeRepository.findAll();
	}

}
