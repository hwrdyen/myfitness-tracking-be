package com.example.cms;
import com.example.cms.controller.exceptions.WorkoutplanNotFoundException;
import com.example.cms.model.entity.WorkoutPlan;
import com.example.cms.model.entity.Client;
import com.example.cms.model.repository.ClientRepository;
import com.example.cms.model.repository.ExerciseRepository;
import com.example.cms.model.repository.WorkoutplanRepository;
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
public class WorkoutPlanTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorkoutplanRepository workoutplanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void getWorkoutplan() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/workoutplans/1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        MockHttpServletResponse response1 = mockMvc.perform(get("/clients/1111"))
                .andReturn().getResponse();

        assertEquals(200, response1.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        ObjectNode receivedJson1 = objectMapper.readValue(response1.getContentAsString(), ObjectNode.class);

        assertEquals(1L, receivedJson.get("id").longValue());
        assertEquals("12/03/2023", receivedJson.get("dates").textValue());
        assertEquals(1111L, receivedJson1.get("id").longValue());
        assertEquals("Tyrion", receivedJson1.get("firstName").textValue());
        assertEquals("Lannister", receivedJson1.get("lastName").textValue());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson1.get("email").textValue());
        assertEquals("Client 1111 - Plan 1", receivedJson.get("planname").textValue());

    }
    @Test
    void addWorkoutplan() throws Exception{

        ObjectNode workoutplanJson = objectMapper.createObjectNode();
        workoutplanJson.put("id", 8888L);
        workoutplanJson.put("dates", "first");
        workoutplanJson.put("clientId", 1111L);
        workoutplanJson.put("planname", "first@last.com");


        MockHttpServletResponse response = mockMvc.perform(
                        post("/workoutplans").
                                contentType("application/json").
                                content(workoutplanJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        assertTrue(workoutplanRepository.findById(8888L).isPresent());
        WorkoutPlan addedworkoutplan = workoutplanRepository.findById(8888L).get();

        MockHttpServletResponse response1 = mockMvc.perform(get("/clients/1111"))
                .andReturn().getResponse();

        assertEquals(200, response1.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response1.getContentAsString(), ObjectNode.class);
        // Assert workoutplan with id 8888 exists in our repository and then get the client object


        // Assert the details of the clients are correct
        assertEquals(8888L, addedworkoutplan.getId());
        assertEquals("first", addedworkoutplan.getDates());
        assertEquals(1111L, receivedJson.get("id").longValue());
        assertEquals("Tyrion", receivedJson.get("firstName").textValue());
        assertEquals("Lannister", receivedJson.get("lastName").textValue());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson.get("email").textValue());
        assertEquals("first@last.com", addedworkoutplan.getPlanname());

    }

    @Test
    void updateWorkoutplan() throws Exception{

        ObjectNode workoutplanJson = objectMapper.createObjectNode();
        workoutplanJson.put("id", 2L);
        workoutplanJson.put("dates", "first");
        workoutplanJson.put("clientId", 1111L);
        workoutplanJson.put("planname", "first@last.com");


        MockHttpServletResponse response = mockMvc.perform(
                        put("/workoutplans/2").
                                contentType("application/json").
                                content(workoutplanJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        assertTrue(workoutplanRepository.findById(2L).isPresent());
        WorkoutPlan addedworkoutplan = workoutplanRepository.findById(2L).get();

        MockHttpServletResponse response1 = mockMvc.perform(get("/clients/1111"))
                .andReturn().getResponse();

        assertEquals(200, response1.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response1.getContentAsString(), ObjectNode.class);
        // Assert workoutplan with id 8888 exists in our repository and then get the client object


        // Assert the details of the clients are correct
        assertEquals(2L, addedworkoutplan.getId());
        assertEquals("first", addedworkoutplan.getDates());
        assertEquals(1111L, receivedJson.get("id").longValue());
        assertEquals("Tyrion", receivedJson.get("firstName").textValue());
        assertEquals("Lannister", receivedJson.get("lastName").textValue());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson.get("email").textValue());
        assertEquals("first@last.com", addedworkoutplan.getPlanname());

    }

    @Test
    void getResult() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/workoutplans/result/1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        MockHttpServletResponse response1 = mockMvc.perform(get("/clients/1111"))
                .andReturn().getResponse();

        assertEquals(200, response1.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        ObjectNode receivedJson1 = objectMapper.readValue(response1.getContentAsString(), ObjectNode.class);

        assertEquals(1L, receivedJson.get("id").longValue());
        assertEquals("12/03/2023", receivedJson.get("dates").textValue());
        assertEquals(1111L, receivedJson1.get("id").longValue());
        assertEquals("Tyrion", receivedJson1.get("firstName").textValue());
        assertEquals("Lannister", receivedJson1.get("lastName").textValue());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson1.get("email").textValue());
        assertEquals("Client 1111 - Plan 1", receivedJson.get("planname").textValue());
        assertEquals(500, receivedJson.get("caloriesburned").doubleValue());
    }



}
