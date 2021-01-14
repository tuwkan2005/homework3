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
import ru.digitalhabbits.homework3.model.DepartmentRequest;
import ru.digitalhabbits.homework3.model.DepartmentResponse;
import ru.digitalhabbits.homework3.model.DepartmentShortResponse;
import ru.digitalhabbits.homework3.service.DepartmentService;

import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.digitalhabbits.homework3.utils.DepartmentHelper.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DepartmentController.class)
class DepartmentControllerTest {

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new GsonBuilder().create();

    @Test
    void departments() throws Exception {

        final DepartmentShortResponse response = buildDepartmentShortResponse();
        when(departmentService.findAllDepartments()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(response.getId()))
                .andExpect(jsonPath("$[0].name").value(response.getName()));
    }

    @Test
    void department() throws Exception {

        final DepartmentResponse response = buildDepartmentResponse();
        when(departmentService.getDepartment(response.getId())).thenReturn(response);

        mockMvc.perform(get(format("/api/v1/departments/%s", response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.closed").value(response.isClosed()))
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.persons[0].id").value(response.getPersons().get(0).getId()))
                .andExpect(jsonPath("$.persons[0].fullName").value(response.getPersons().get(0).getFullName()));
    }

    @Test
    void createDepartment() throws Exception {

        final DepartmentRequest request = buildDepartmentRequest();
        final DepartmentResponse response = buildDepartmentResponseFromRequest(request);
        when(departmentService.createDepartment(request)).thenReturn(response.getId());

        mockMvc.perform(post("/api/v1/departments/")
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern(format("http://*/api/v1/departments/%s", response.getId())));
    }

    @Test
    void updateDepartment() throws Exception {

        final DepartmentRequest request = buildDepartmentRequest();
        final DepartmentResponse response = buildDepartmentResponse();
        when(departmentService.updateDepartment(response.getId(), request)).thenReturn(response);

        mockMvc.perform(patch(format("/api/v1/departments/%s", response.getId()))
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.closed").value(response.isClosed()))
                .andExpect(jsonPath("$.persons").isArray());
    }

    @Test
    void deleteDepartment() throws Exception {

        Integer departmentId = nextInt();
        doNothing().when(departmentService).deleteDepartment(departmentId);

        mockMvc.perform(delete(format("/api/v1/departments/%s", departmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).deleteDepartment(departmentId);
    }

    @Test
    void addPersonToDepartment() throws Exception {

        Integer personId = nextInt();
        Integer departmentId = nextInt();
        doNothing().when(departmentService).addPersonToDepartment(departmentId, personId);

        mockMvc.perform(post(format("/api/v1/departments/%s/%s", departmentId, personId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).addPersonToDepartment(departmentId, personId);
    }

    @Test
    void removePersonFromDepartment() throws Exception {

        Integer personId = nextInt();
        Integer departmentId = nextInt();
        doNothing().when(departmentService).removePersonFromDepartment(departmentId, personId);

        mockMvc.perform(delete(format("/api/v1/departments/%s/%s", departmentId, personId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).removePersonFromDepartment(departmentId, personId);
    }

    @Test
    void closeDepartment() throws Exception {

        Integer departmentId = nextInt();
        doNothing().when(departmentService).closeDepartment(departmentId);

        mockMvc.perform(post(format("/api/v1/departments/%s/close", departmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).closeDepartment(departmentId);
    }
}