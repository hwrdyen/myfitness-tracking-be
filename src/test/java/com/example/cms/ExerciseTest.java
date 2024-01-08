package com.example.cms;
import com.example.cms.controller.exceptions.ExerciseNotFoundException;
import com.example.cms.model.entity.Client;
import com.example.cms.model.entity.Exercise;
import com.example.cms.model.repository.ClientRepository;
import com.example.cms.model.repository.ExerciseRepository;
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
public class ExerciseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void getExercise() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/exercises/1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(1L, receivedJson.get("id").longValue());
        assertEquals("Benchpress", receivedJson.get("name").textValue());
        assertEquals(100, receivedJson.get("calories").doubleValue());
        assertEquals("https://vimeo.com/178057019", receivedJson.get("video_link").textValue());
        assertEquals("https://www.inspireusafoundation.org/wp-content/uploads/2021/03/bench-press-muscles.jpg",
                receivedJson.get("image_link").textValue());
    }

    @Test
    void addExercise() throws Exception{

        ObjectNode exerciseJson = objectMapper.createObjectNode();
        exerciseJson.put("id", 8888L);
        exerciseJson.put("name", "first");
        exerciseJson.put("calories", 0);
        exerciseJson.put("video_link", "first@last.com");
        exerciseJson.put("image_link", "first@last.com");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/exercises").
                                contentType("application/json").
                                content(exerciseJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        // Assert client with id 8888 exists in our repository and then get the client object
        assertTrue(exerciseRepository.findById(8888L).isPresent());
        Exercise addedExercise = exerciseRepository.findById(8888L).get();

        // Assert the details of the exercises are correct
        assertEquals(8888L, addedExercise.getId());
        assertEquals("first", addedExercise.getName());
        assertEquals(0, addedExercise.getCalories());
        assertEquals("first@last.com", addedExercise.getVideo_link());
        assertEquals("first@last.com", addedExercise.getImage_link());
    }

    @Test
    void updateExercise() throws Exception{

        ObjectNode exerciseJson = objectMapper.createObjectNode();
        exerciseJson.put("id", 0L);
        exerciseJson.put("name", "first");
        exerciseJson.put("calories", 0);
        exerciseJson.put("video_link", "first@last.com");
        exerciseJson.put("image_link", "first@last.com");

        MockHttpServletResponse response = mockMvc.perform(
                        put("/exercises/0").
                                contentType("application/json").
                                content(exerciseJson.toString()))
                .andReturn().getResponse();

        // assert HTTP code of response is 200 OK
        assertEquals(200, response.getStatus());

        // Assert client with id 8888 exists in our repository and then get the client object
        assertTrue(exerciseRepository.findById(0L).isPresent());
        Exercise addedExercise = exerciseRepository.findById(0L).get();

        // Assert the details of the exercises are correct
        assertEquals(0L, addedExercise.getId());
        assertEquals("first", addedExercise.getName());
        assertEquals(0, addedExercise.getCalories());
        assertEquals("first@last.com", addedExercise.getVideo_link());
        assertEquals("first@last.com", addedExercise.getImage_link());
    }

    @Test
    void deleteExercise() throws Exception{
        Exercise e = new Exercise();
        e.setId(123456L);
        e.setName("first");
        e.setCalories(90.0);
        e.setVideo_link("first@last.com");
        e.setImage_link("first@last.com");
        exerciseRepository.save(e);

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/exercises/123456").
                                contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertTrue(exerciseRepository.findById(123456L).isEmpty());
    }


}
