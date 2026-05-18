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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerRepository ownerRepository;

    @MockitoBean
    private PetRepository petRepository;

    @MockitoBean
    private VisitRepository visitRepository;

    @Test
    @DisplayName("POST /api/owners/{ownerId}/pets/{petId}/visits - registrar visita")
    void shouldRegisterVisitForPet() throws Exception {
        Pet pet = new Pet();
        pet.setId(10);

        Visit visit = new Visit();
        visit.setId(20);
        visit.setDate(LocalDate.now().plusDays(1));
        visit.setDescription("retorno");

        when(ownerRepository.existsById(1)).thenReturn(true);
        when(petRepository.findById(10)).thenReturn(Optional.of(pet));
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        String json = "{\"date\":\"%s\",\"description\":\"retorno\"}"
            .formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/owners/1/pets/10/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(20))
            .andExpect(jsonPath("$.description").value("retorno"));
    }

    @Test
    @DisplayName("GET /api/owners/{ownerId}/pets/{petId}/visits - listar visitas")
    void shouldListVisitsForPet() throws Exception {
        Visit visit = new Visit();
        visit.setId(20);
        visit.setDate(LocalDate.of(2026, 6, 10));
        visit.setDescription("vacina");

        when(ownerRepository.existsById(1)).thenReturn(true);
        when(petRepository.existsById(10)).thenReturn(true);
        when(visitRepository.findByPetId(10)).thenReturn(List.of(visit));

        mockMvc.perform(get("/api/owners/1/pets/10/visits"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(20))
            .andExpect(jsonPath("$[0].description").value("vacina"));
    }

    @Test
    @DisplayName("GET /api/owners/{ownerId}/pets/{petId}/visits - owner inexistente")
    void shouldReturnNotFoundWhenOwnerDoesNotExist() throws Exception {
        when(ownerRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(get("/api/owners/99/pets/10/visits"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/owners/{ownerId}/pets/{petId}/visits - pet inexistente")
    void shouldReturnNotFoundWhenPetDoesNotExistOnListVisits() throws Exception {
        when(ownerRepository.existsById(1)).thenReturn(true);
        when(petRepository.existsById(999)).thenReturn(false);

        mockMvc.perform(get("/api/owners/1/pets/999/visits"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/owners/{ownerId}/pets/{petId}/visits - owner inexistente")
    void shouldReturnNotFoundWhenCreatingVisitForNonexistentOwner() throws Exception {
        when(ownerRepository.existsById(99)).thenReturn(false);

        String json = "{\"date\":\"%s\",\"description\":\"retorno\"}"
            .formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/owners/99/pets/10/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/owners/{ownerId}/pets/{petId}/visits - pet inexistente")
    void shouldReturnNotFoundWhenCreatingVisitForNonexistentPet() throws Exception {
        when(ownerRepository.existsById(1)).thenReturn(true);
        when(petRepository.findById(404)).thenReturn(Optional.empty());

        String json = "{\"date\":\"%s\",\"description\":\"retorno\"}"
            .formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/owners/1/pets/404/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/owners/{ownerId}/pets/{petId}/visits - payload inválido")
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
                String json = "{\"date\":null,\"description\":\"\"}";

        mockMvc.perform(post("/api/owners/1/pets/10/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }
}
