package br.com.convergencia.testejavar1.controller;

import br.com.convergencia.testejavar1.controller.form.CpfForm;
import br.com.convergencia.testejavar1.controller.form.UpdateCpfStatusForm;
import br.com.convergencia.testejavar1.model.CpfModel;
import br.com.convergencia.testejavar1.model.enums.StatusEnum;
import br.com.convergencia.testejavar1.repository.CpfRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CpfResource.class)
class CpfResourceTest {

    @MockBean
    CpfRepository repositoryMck;

    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName("should register new cpf")
    void shouldRegisterNewCpf() throws Exception {
        // given
        given(repositoryMck.findByNumber(any()))
                .willReturn(Optional.empty());

        given(repositoryMck.save(any()))
                .willReturn(new CpfModel("02370453117"));

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/cpfs")
                .content(asJsonString(new CpfForm("023.704.531-17")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("02370453117"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/cpfs/02370453117"));
    }

    @Test
    @DisplayName("should find by cpf")
    void shouldFindByCpf() throws Exception {
        // given
        given(repositoryMck.findByNumber(any()))
                .willReturn(Optional.of(new CpfModel("02370453117")));
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/cpfs/02370453117")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("02370453117"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("should update cpf")
    void shouldUpdateCpf() throws Exception {
        // given
        given(repositoryMck.findByNumber(any()))
                .willReturn(Optional.of(new CpfModel("02370453117")));

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch("/cpfs/02370453117")
                .content(asJsonString(new UpdateCpfStatusForm(StatusEnum.INACTIVE)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("02370453117"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    @DisplayName("should thrown exception cpf already registered")
    void shouldThrownCpfAlreadyRegistered() throws Exception {
        // given
        given(repositoryMck.findByNumber(any()))
                .willReturn(Optional.of(new CpfModel("02370453117")));

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/cpfs")
                .content(asJsonString(new CpfForm("023.704.531-17")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.message").value("cpf already registered"))
                .andExpect(jsonPath("$.detail").value("you already have a registered cpf"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}