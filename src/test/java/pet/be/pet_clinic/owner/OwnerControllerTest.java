package pet.be.pet_clinic.owner;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerRepository ownerRepository;

    @Test
    @DisplayName("POST /api/owners - criar owner válido")
    void shouldCreateOwnerWithValidData() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("Rua 1");
        owner.setCity("Cidade");
        owner.setTelephone("11999999999");
        owner.setEmail("john@doe.com");

        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        String json = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"Rua 1\","
            + "\"city\":\"Cidade\",\"telephone\":\"11999999999\",\"email\":\"john@doe.com\"}";

        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.email").value("john@doe.com"));
    }

    @Test
    @DisplayName("POST /api/owners - dados inválidos")
    void shouldFailToCreateOwnerWithInvalidData() throws Exception {
        String json = "{\"firstName\":\"\",\"lastName\":\"\",\"address\":\"Rua 1\","
            + "\"city\":\"Cidade\",\"telephone\":\"abc\",\"email\":\"not-an-email\"}";

        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/owners/{id} - buscar existente")
    void shouldGetOwnerById() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("Rua 1");
        owner.setCity("Cidade");
        owner.setTelephone("11999999999");
        owner.setEmail("john@doe.com");

        when(ownerRepository.findById(1)).thenReturn(java.util.Optional.of(owner));

        mockMvc.perform(get("/api/owners/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.email").value("john@doe.com"));
    }

    @Test
    @DisplayName("GET /api/owners/{id} - não encontrado")
    void shouldReturnNotFoundForNonexistentOwner() throws Exception {
        when(ownerRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/owners/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/owners - listar owners")
    void shouldListOwners() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("Rua 1");
        owner.setCity("Cidade");
        owner.setTelephone("11999999999");
        owner.setEmail("john@doe.com");

        when(ownerRepository.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(List.of(owner)));

        mockMvc.perform(get("/api/owners"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].firstName").value("John"));
    }

    @Test
    @DisplayName("GET /api/owners?name=... - listar owners filtrando por nome")
    void shouldListOwnersUsingNameFilter() throws Exception {
        Owner owner = new Owner();
        owner.setId(7);
        owner.setFirstName("Johnny");
        owner.setLastName("Doe");
        owner.setAddress("Rua 1");
        owner.setCity("Cidade");
        owner.setTelephone("11999999999");
        owner.setEmail("johnny@doe.com");

        when(ownerRepository.findByName("john", PageRequest.of(0, 20))).thenReturn(new PageImpl<>(List.of(owner)));

        mockMvc.perform(get("/api/owners").param("name", "john"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(7))
            .andExpect(jsonPath("$.content[0].firstName").value("Johnny"));
    }

    @Test
    @DisplayName("PUT /api/owners/{id} - atualizar owner existente")
    void shouldUpdateExistingOwner() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("Rua antiga");
        owner.setCity("Cidade antiga");
        owner.setTelephone("11999999999");
        owner.setEmail("john@doe.com");

        when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        String json = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"Rua nova\","
            + "\"city\":\"Nova cidade\",\"telephone\":\"11988887777\",\"email\":\"john.new@doe.com\"}";

        mockMvc.perform(put("/api/owners/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));

        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    @DisplayName("PUT /api/owners/{id} - owner inexistente")
    void shouldReturnNotFoundWhenUpdatingNonexistentOwner() throws Exception {
        when(ownerRepository.findById(404)).thenReturn(Optional.empty());

        String json = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"Rua 1\","
            + "\"city\":\"Cidade\",\"telephone\":\"11999999999\",\"email\":\"john@doe.com\"}";

        mockMvc.perform(put("/api/owners/404")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/owners/{id} - dados inválidos")
    void shouldReturnBadRequestWhenUpdatingWithInvalidData() throws Exception {
        String json = "{\"firstName\":\"\",\"lastName\":\"\",\"address\":\"Rua 1\","
            + "\"city\":\"Cidade\",\"telephone\":\"abc\",\"email\":\"invalid\"}";

        mockMvc.perform(put("/api/owners/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/owners/{id} - owner existente")
    void shouldDeleteOwner() throws Exception {
        when(ownerRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/owners/1"))
            .andExpect(status().isNoContent());

        verify(ownerRepository).deleteById(1);
    }

    @Test
    @DisplayName("DELETE /api/owners/{id} - owner inexistente")
    void shouldReturnNotFoundWhenDeletingNonexistentOwner() throws Exception {
        when(ownerRepository.existsById(77)).thenReturn(false);

        mockMvc.perform(delete("/api/owners/77"))
            .andExpect(status().isNotFound());
    }
}
