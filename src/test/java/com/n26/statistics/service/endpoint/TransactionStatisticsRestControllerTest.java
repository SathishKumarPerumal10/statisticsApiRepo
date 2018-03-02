package com.n26.statistics.service.endpoint;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.n26.statistics.endpoint.rest.TransactionStatisticsRestController;
import com.n26.statistics.exception.InValidTransactionException;
import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.service.TransactionStatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TransactionStatisticsRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionStatisticsService transactionStatisticsService;

    @InjectMocks
    private TransactionStatisticsRestController transactionStatisticsRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionStatisticsRestController).build();
    }

    @Test
    public void whenCreateTransactionRequest_thenReturnResponseWithStatusCreated() throws Exception {

        String requestBody = "{\"amount\":123.00,\"timestamp\":1519986840162}";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/transactions").content(requestBody).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    public void whenCreateTransactionRequestwithInvalidRequest_thenReturnResponseWithStatusNoContent() throws Exception {

        doThrow(InValidTransactionException.class).when(transactionStatisticsService).createTransactionStatistics(any(TransactionRequestDto.class));
        String requestBody = "{\"amount\":123.00,\"timestamp\":1519986840162}";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/transactions").content(requestBody).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void whenGetStatistics_thenReturnStatistics() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/statistics").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        assertNotNull(result);
    }

}
