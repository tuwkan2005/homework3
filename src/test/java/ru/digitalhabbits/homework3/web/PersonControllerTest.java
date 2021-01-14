package ru.digitalhabbits.homework3.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.digitalhabbits.homework3.model.PersonRequest;
import ru.digitalhabbits.homework3.model.PersonResponse;
import ru.digitalhabbits.homework3.service.PersonService;

import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.digitalhabbits.homework3.utils.PersonHelper.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @MockBean
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new GsonBuilder().create();

    @Test
    void persons() throws Exception {

        final PersonResponse response = buildPersonResponse();
        when(personService.findAllPersons()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(response.getId()))
                .andExpect(jsonPath("$[0].fullName").value(response.getFullName()))
                .andExpect(jsonPath("$[0].age").value(response.getAge()))
                .andExpect(jsonPath("$[0].department").value(response.getDepartment()))
                .andExpect(jsonPath("$[0].department.name").value(response.getDepartment().getName()))
                .andExpect(jsonPath("$[0].department.id").value(response.getDepartment().getId()));
    }

    @Test
    void person() throws Exception {

        final PersonResponse response = buildPersonResponse();
        when(personService.getPerson(response.getId())).thenReturn(response);

        mockMvc.perform(get(format("/api/v1/persons/%s", response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.fullName").value(response.getFullName()))
                .andExpect(jsonPath("$.age").value(response.getAge()))
                .andExpect(jsonPath("$.department").value(response.getDepartment()))
                .andExpect(jsonPath("$.department.name").value(response.getDepartment().getName()))
                .andExpect(jsonPath("$.department.id").value(response.getDepartment().getId()));
    }

    @Test
    void createPerson() throws Exception {

        final PersonRequest request = buildPersonRequest();
        final PersonResponse response = buildPersonResponseFromRequest(request);
        when(personService.createPerson(request)).thenReturn(response.getId());

        mockMvc.perform(post("/api/v1/persons/")
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern(format("http://*/api/v1/persons/%s", response.getId())));

    }

    @Test
    void updatePerson() throws Exception {

        final PersonRequest request = buildPersonRequest();
        final PersonResponse response = buildPersonResponse();
        when(personService.updatePerson(response.getId(), request)).thenReturn(response);

        mockMvc.perform(patch(format("/api/v1/persons/%s", response.getId()))
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.fullName").value(response.getFullName()))
                .andExpect(jsonPath("$.age").value(response.getAge()))
                .andExpect(jsonPath("$.department").value(response.getDepartment()))
                .andExpect(jsonPath("$.department.name").value(response.getDepartment().getName()))
                .andExpect(jsonPath("$.department.id").value(response.getDepartment().getId()));
    }

    @Test
    void deletePerson() throws Exception {

        Integer personId = nextInt();
        doNothing().when(personService).deletePerson(personId);

        mockMvc.perform(delete(format("/api/v1/persons/%s", personId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(personService, times(1)).deletePerson(personId);

    }
}