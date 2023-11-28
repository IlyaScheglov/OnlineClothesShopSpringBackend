package com.example.NewProject.controllers;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainPagesControllerTest {

    @Autowired
    private MainPagesController mainPagesController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginRedirectionTest() throws Exception{
        this.mockMvc.perform(get("/shop-page"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void correctLogin() throws Exception{
        this.mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin().user("ilya").password("12345"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Ignore
    @Test
    public void notCorrectLogin() throws Exception{
        this.mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin().user("frvfrvv").password("12345"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
