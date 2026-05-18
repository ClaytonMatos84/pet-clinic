package pet.be.pet_clinic.vet;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VetController.class)
class VetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VetRepository vetRepository;

    @Test
    @DisplayName("GET /api/vets - listar todos")
    void shouldListAllVets() throws Exception {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("radiology");

        Vet vet = new Vet();
        vet.setId(4);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        vet.setSpecialties(Set.of(specialty));

        when(vetRepository.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(List.of(vet)));

        mockMvc.perform(get("/api/vets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(4))
            .andExpect(jsonPath("$.content[0].firstName").value("James"))
            .andExpect(jsonPath("$.content[0].specialties[0]").value("radiology"));
    }

    @Test
    @DisplayName("GET /api/vets - resposta vazia")
    void shouldReturnEmptyPageWhenNoVetsExist() throws Exception {
        when(vetRepository.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/vets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content").isEmpty());
    }
}
