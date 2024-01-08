package com.example.cms;
import com.example.cms.controller.exceptions.ClientNotFoundException;
import com.example.cms.model.entity.Client;
import com.example.cms.model.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void getClient() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/clients/1111"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(1111L, receivedJson.get("id").longValue());
        assertEquals("Tyrion", receivedJson.get("firstName").textValue());
        assertEquals("Lannister", receivedJson.get("lastName").textValue());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson.get("email").textValue());
    }

    @Test
    void addClient() throws Exception{

        ObjectNode clientJson = objectMapper.createObjectNode();
        clientJson.put("id", 8888L);
        clientJson.put("firstName", "first");
        clientJson.put("lastName", "last");
        clientJson.put("email", "first@last.com");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/clients").
                                contentType("application/json").
                                content(clientJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        // Assert client with id 8888 exists in our repository and then get the client object
        assertTrue(clientRepository.findById(8888L).isPresent());
        Client addedStudent = clientRepository.findById(8888L).get();

        // Assert the details of the clients are correct
        assertEquals(8888L, addedStudent.getId());
        assertEquals("first", addedStudent.getFirstName());
        assertEquals("last", addedStudent.getLastName());
        assertEquals("first@last.com", addedStudent.getEmail());
    }
    @Test
    void updateClient() throws Exception{

        ObjectNode clientJson = objectMapper.createObjectNode();
        clientJson.put("id", 2222L);
        clientJson.put("firstName", "first");
        clientJson.put("lastName", "last");
        clientJson.put("email", "first@last.com");

        MockHttpServletResponse response = mockMvc.perform(
                        put("/clients/2222").
                                contentType("application/json").
                                content(clientJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        // Assert client with id 8888 exists in our repository and then get the client object
        assertTrue(clientRepository.findById(2222L).isPresent());
        Client addedStudent = clientRepository.findById(2222L).get();

        // Assert the details of the clients are correct
        assertEquals(2222L, addedStudent.getId());
        assertEquals("first", addedStudent.getFirstName());
        assertEquals("last", addedStudent.getLastName());
        assertEquals("first@last.com", addedStudent.getEmail());
    }

    @Test
    void deleteStudent() throws Exception{
        Client c = new Client();
        c.setId(123456L);
        c.setFirstName("first");
        c.setLastName("last");
        c.setEmail("first@last.com");
        clientRepository.save(c);

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/clients/123456").
                                contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertTrue(clientRepository.findById(123456L).isEmpty());
    }


}
