package com.example.cms;
import com.example.cms.controller.exceptions.WorkoutplanNotFoundException;
import com.example.cms.model.entity.WorkoutPlan;
import com.example.cms.model.entity.Exercise;
import com.example.cms.model.entity.PlanDetail;
import com.example.cms.model.entity.PlanDetailKey;
import com.example.cms.model.repository.ClientRepository;
import com.example.cms.model.repository.PlanDetailRepository;
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
public class PlanDetailTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorkoutplanRepository workoutplanRepository;

    @Autowired
    private PlanDetailRepository planRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void getPlan() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/plans/1/1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        MockHttpServletResponse response1 = mockMvc.perform(get("/workoutplans/1"))
                .andReturn().getResponse();

        assertEquals(200, response1.getStatus());

        MockHttpServletResponse response2 = mockMvc.perform(get("/exercises/1"))
                .andReturn().getResponse();

        assertEquals(200, response2.getStatus());

        MockHttpServletResponse response3 = mockMvc.perform(get("/clients/1111"))
                .andReturn().getResponse();

        assertEquals(200, response3.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        ObjectNode receivedJson1 = objectMapper.readValue(response1.getContentAsString(), ObjectNode.class);
        ObjectNode receivedJson2 = objectMapper.readValue(response2.getContentAsString(), ObjectNode.class);
        ObjectNode receivedJson3 = objectMapper.readValue(response3.getContentAsString(), ObjectNode.class);

        assertEquals(1L, receivedJson1.get("id").longValue());
        assertEquals(1L, receivedJson2.get("id").longValue());
        assertEquals(1L, receivedJson1.get("id").longValue());
        assertEquals("12/03/2023", receivedJson1.get("dates").textValue());
        assertEquals(1111L, receivedJson3.get("id").longValue());
        assertEquals("Tyrion", receivedJson3.get("firstName").textValue());
        assertEquals("Lannister", receivedJson3.get("lastName").textValue());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson3.get("email").textValue());
        assertEquals("Client 1111 - Plan 1", receivedJson1.get("planname").textValue());
        assertEquals(1L, receivedJson2.get("id").longValue());
        assertEquals("Benchpress", receivedJson2.get("name").textValue());
        assertEquals(100, receivedJson2.get("calories").doubleValue());
        assertEquals("https://vimeo.com/178057019", receivedJson2.get("video_link").textValue());
        assertEquals("https://www.inspireusafoundation.org/wp-content/uploads/2021/03/bench-press-muscles.jpg",
                receivedJson2.get("image_link").textValue());
        assertEquals(2, receivedJson.get("duration").doubleValue());

    }

    @Test
    void addPlan() throws Exception{

        ObjectNode planJson = objectMapper.createObjectNode();
        planJson.put("workoutplanId", 1L);
        planJson.put("exerciseId", 10L);
        planJson.put("duration", 5);

        PlanDetailKey key = new PlanDetailKey();
        key.setWorkoutplanId(1L);
        key.setExerciseId(10L);

        MockHttpServletResponse response = mockMvc.perform(
                        post("/plans").
                                contentType("application/json").
                                content(planJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        assertTrue(planRepository.findById(key).isPresent());
        PlanDetail addedplan = planRepository.findById(key).get();

        MockHttpServletResponse response1 = mockMvc.perform(get("/workoutplans/1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        MockHttpServletResponse response2 = mockMvc.perform(get("/exercises/10"))
                .andReturn().getResponse();

        assertEquals(200, response2.getStatus());

        MockHttpServletResponse response3 = mockMvc.perform(get("/clients/1111"))
                .andReturn().getResponse();

        assertEquals(200, response3.getStatus());

        ObjectNode receivedJson1 = objectMapper.readValue(response1.getContentAsString(), ObjectNode.class);
        ObjectNode receivedJson2 = objectMapper.readValue(response2.getContentAsString(), ObjectNode.class);
        ObjectNode receivedJson3 = objectMapper.readValue(response3.getContentAsString(), ObjectNode.class);


        // Assert the details of the clients are correct
        assertEquals(1L, receivedJson1.get("id").longValue());
        assertEquals(10L, receivedJson2.get("id").longValue());
        assertEquals(1L, receivedJson1.get("id").longValue());
        assertEquals("12/03/2023", receivedJson1.get("dates").textValue());
        assertEquals(1111L, receivedJson3.get("id").longValue());
        assertEquals("Tyrion", receivedJson3.get("firstName").textValue());
        assertEquals("Lannister", receivedJson3.get("lastName").textValue());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson3.get("email").textValue());
        assertEquals("Client 1111 - Plan 1", receivedJson1.get("planname").textValue());
        assertEquals(10L, receivedJson2.get("id").longValue());
        assertEquals("Forearm curl in", receivedJson2.get("name").textValue());
        assertEquals(100, receivedJson2.get("calories").doubleValue());
        assertEquals("https://vimeo.com/178047269", receivedJson2.get("video_link").textValue());
        assertEquals("https://fitliferegime.com/wp-content/uploads/2023/06/Seated-Palms-Up-Wrist-Curl.jpg",
                receivedJson2.get("image_link").textValue());
        assertEquals(5, addedplan.getDuration());

    }



}
