package pet.be.pet_clinic.owner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerRepository ownerRepository;

    @MockitoBean
    private PetRepository petRepository;

    @MockitoBean
    private PetTypeRepository petTypeRepository;

    @Test
    @DisplayName("POST /api/owners/{ownerId}/pets - adicionar pet válido")
    void shouldAddPetToOwner() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);

        PetType petType = new PetType();
        petType.setId(3);
        petType.setName("dog");

        Pet pet = new Pet();
        pet.setId(10);
        pet.setName("Rex");
        pet.setBirthDate(LocalDate.of(2020, 1, 10));
        pet.setType(petType);
        pet.setOwner(owner);

        when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));
        when(petTypeRepository.findById(3)).thenReturn(Optional.of(petType));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

                String json = "{\"name\":\"Rex\",\"birthDate\":\"2020-01-10\",\"typeId\":3}";

        mockMvc.perform(post("/api/owners/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.name").value("Rex"))
            .andExpect(jsonPath("$.type").value("dog"));
    }

    @Test
    @DisplayName("POST /api/owners/{ownerId}/pets - owner inexistente")
    void shouldReturnNotFoundWhenOwnerDoesNotExist() throws Exception {
        when(ownerRepository.findById(99)).thenReturn(Optional.empty());

                String json = "{\"name\":\"Rex\",\"birthDate\":\"2020-01-10\",\"typeId\":3}";

        mockMvc.perform(post("/api/owners/99/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/owners/{ownerId}/pets - payload inválido")
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
                String json = "{\"name\":\"\",\"birthDate\":null,\"typeId\":null}";

        mockMvc.perform(post("/api/owners/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/owners/{ownerId}/pets - listar pets")
    void shouldListPetsForOwner() throws Exception {
        PetType petType = new PetType();
        petType.setId(3);
        petType.setName("dog");

        Pet pet = new Pet();
        pet.setId(10);
        pet.setName("Rex");
        pet.setBirthDate(LocalDate.of(2020, 1, 10));
        pet.setType(petType);

        when(ownerRepository.existsById(1)).thenReturn(true);
        when(petRepository.findByOwnerId(1)).thenReturn(List.of(pet));

        mockMvc.perform(get("/api/owners/1/pets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(10))
            .andExpect(jsonPath("$[0].name").value("Rex"));
    }

        @Test
        @DisplayName("GET /api/owners/{ownerId}/pets - owner inexistente")
        void shouldReturnNotFoundWhenListingPetsForNonexistentOwner() throws Exception {
                when(ownerRepository.existsById(99)).thenReturn(false);

                mockMvc.perform(get("/api/owners/99/pets"))
                        .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("POST /api/owners/{ownerId}/pets - tipo inválido")
        void shouldReturnBadRequestWhenPetTypeIsInvalid() throws Exception {
                Owner owner = new Owner();
                owner.setId(1);

                when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));
                when(petTypeRepository.findById(999)).thenReturn(Optional.empty());

                String json = "{\"name\":\"Rex\",\"birthDate\":\"2020-01-10\",\"typeId\":999}";

                mockMvc.perform(post("/api/owners/1/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("PUT /api/owners/{ownerId}/pets/{petId} - atualizar pet")
        void shouldUpdatePet() throws Exception {
                PetType petType = new PetType();
                petType.setId(3);
                petType.setName("dog");

                Pet pet = new Pet();
                pet.setId(10);
                pet.setName("Rex");
                pet.setBirthDate(LocalDate.of(2020, 1, 10));
                pet.setType(petType);

                when(ownerRepository.existsById(1)).thenReturn(true);
                when(petRepository.findById(10)).thenReturn(Optional.of(pet));
                when(petTypeRepository.findById(3)).thenReturn(Optional.of(petType));
                when(petRepository.save(any(Pet.class))).thenReturn(pet);

                String json = "{\"name\":\"Rex Jr\",\"birthDate\":\"2021-01-10\",\"typeId\":3}";

                mockMvc.perform(put("/api/owners/1/pets/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(10));

                verify(petRepository).save(any(Pet.class));
        }

        @Test
        @DisplayName("PUT /api/owners/{ownerId}/pets/{petId} - owner inexistente")
        void shouldReturnNotFoundWhenUpdatingPetForNonexistentOwner() throws Exception {
                when(ownerRepository.existsById(99)).thenReturn(false);

                String json = "{\"name\":\"Rex\",\"birthDate\":\"2020-01-10\",\"typeId\":3}";

                mockMvc.perform(put("/api/owners/99/pets/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PUT /api/owners/{ownerId}/pets/{petId} - pet inexistente")
        void shouldReturnNotFoundWhenUpdatingNonexistentPet() throws Exception {
                when(ownerRepository.existsById(1)).thenReturn(true);
                when(petRepository.findById(404)).thenReturn(Optional.empty());

                String json = "{\"name\":\"Rex\",\"birthDate\":\"2020-01-10\",\"typeId\":3}";

                mockMvc.perform(put("/api/owners/1/pets/404")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PUT /api/owners/{ownerId}/pets/{petId} - tipo inválido")
        void shouldReturnBadRequestWhenUpdatingPetWithInvalidType() throws Exception {
                Pet pet = new Pet();
                pet.setId(10);

                when(ownerRepository.existsById(1)).thenReturn(true);
                when(petRepository.findById(10)).thenReturn(Optional.of(pet));
                when(petTypeRepository.findById(999)).thenReturn(Optional.empty());

                String json = "{\"name\":\"Rex\",\"birthDate\":\"2020-01-10\",\"typeId\":999}";

                mockMvc.perform(put("/api/owners/1/pets/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isBadRequest());
        }
}
