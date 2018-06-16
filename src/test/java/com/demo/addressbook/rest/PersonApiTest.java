package com.demo.addressbook.rest;

import com.demo.addressbook.AddressBookApp;
import com.demo.addressbook.model.Person;
import com.demo.addressbook.reepository.PersonRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AddressBookApp.class)
@Transactional
public class PersonApiTest {

    private HttpMessageConverter<Object> mappingJackson2HttpMessageConverter;

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    /**
     * Test for {@link PersonApi#create(Person)}
     */
    @Test
    public void create() throws Exception {
        final Person person = new Person();
        person.setFirstName("foo");
        final ResultActions perform = mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(person)));
        perform.andExpect(status().isOk());
    }

    /**
     * Test for {@link PersonApi#create(Person)}
     */
    @Test
    public void createMissingName() throws Exception {
        final Person person = new Person();
        person.setLastName("foo");
        final ResultActions perform = mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(person)));
        perform.andExpect(status().isBadRequest());
    }

    /**
     * Test for {@link PersonApi#deletePerson(long)}
     */
    @Test
    public void deletePersonNoPerson() throws Exception {
        final ResultActions perform = mockMvc.perform(delete("/api/persons/1"));
        perform.andExpect(status().isBadRequest());
    }

    /**
     * Test for {@link PersonApi#deletePerson(long)}
     */
    @Test
    public void deletePerson() throws Exception {

        final Person person = new Person();
        person.setFirstName("foo");
        personRepository.save(person);

        final ResultActions perform = mockMvc.perform(delete("/api/persons/" + person.getId()));
        perform.andExpect(status().isOk());
    }


    protected String json(Object object) throws IOException {
        final MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(object, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Autowired
    void setConverters(HttpMessageConverter<Object>[] converters) {
        mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        Assert.assertNotNull("The JSON message converter must not be null", mappingJackson2HttpMessageConverter);
    }
}
