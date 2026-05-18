package pet.be.pet_clinic.owner;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetTypeController.class)
class PetTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetTypeRepository petTypeRepository;

    @Test
    @DisplayName("GET /api/pettypes - listar tipos")
    void shouldListPetTypes() throws Exception {
        PetType dog = new PetType();
        dog.setId(1);
        dog.setName("dog");

        PetType cat = new PetType();
        cat.setId(2);
        cat.setName("cat");

        when(petTypeRepository.findAll()).thenReturn(List.of(dog, cat));

        mockMvc.perform(get("/api/pettypes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("dog"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("cat"));
    }

    @Test
    @DisplayName("GET /api/pettypes - vazio")
    void shouldReturnEmptyListWhenNoPetTypesExist() throws Exception {
        when(petTypeRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/pettypes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }
}
