package com.example.cms;
import com.example.cms.controller.exceptions.AdministratorNotFoundException;
import com.example.cms.model.entity.Administrator;
import com.example.cms.model.entity.Client;
import com.example.cms.model.repository.AdministratorRepository;
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
public class AdminTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Test
    void getAdmin() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/administrators/1234"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(1234L, receivedJson.get("id").longValue());
        assertEquals("Admin", receivedJson.get("firstName").textValue());
        assertEquals("A", receivedJson.get("lastName").textValue());
        assertEquals("admin.a@mail.univ.ca", receivedJson.get("email").textValue());
    }
    @Test
    void addClient() throws Exception{

        ObjectNode clientJson = objectMapper.createObjectNode();
        clientJson.put("id", 8888L);
        clientJson.put("firstName", "first");
        clientJson.put("lastName", "last");
        clientJson.put("email", "first@last.com");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/administrators").
                                contentType("application/json").
                                content(clientJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        // Assert client with id 8888 exists in our repository and then get the client object
        assertTrue(administratorRepository.findById(8888L).isPresent());
        Administrator addedAdmin = administratorRepository.findById(8888L).get();

        // Assert the details of the clients are correct
        assertEquals(8888L, addedAdmin.getId());
        assertEquals("first", addedAdmin.getFirstName());
        assertEquals("last", addedAdmin.getLastName());
        assertEquals("first@last.com", addedAdmin.getEmail());
    }

    @Test
    void updateAdmin() throws Exception{

        ObjectNode clientJson = objectMapper.createObjectNode();
        clientJson.put("id", 2345L);
        clientJson.put("firstName", "first");
        clientJson.put("lastName", "last");
        clientJson.put("email", "first@last.com");

        MockHttpServletResponse response = mockMvc.perform(
                        put("/administrators/2345").
                                contentType("application/json").
                                content(clientJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        // Assert client with id 8888 exists in our repository and then get the client object
        assertTrue(administratorRepository.findById(2345L).isPresent());
        Administrator addedAdmin = administratorRepository.findById(2345L).get();

        // Assert the details of the clients are correct
        assertEquals(2345L, addedAdmin.getId());
        assertEquals("first", addedAdmin.getFirstName());
        assertEquals("last", addedAdmin.getLastName());
        assertEquals("first@last.com", addedAdmin.getEmail());
    }

    @Test
    void deleteAdmin() throws Exception{
        Administrator a = new Administrator();
        a.setId(123456L);
        a.setFirstName("first");
        a.setLastName("last");
        a.setEmail("first@last.com");
        administratorRepository.save(a);

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/administrators/123456").
                                contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertTrue(administratorRepository.findById(123456L).isEmpty());
    }

}
