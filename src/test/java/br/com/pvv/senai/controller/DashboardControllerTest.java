package br.com.pvv.senai.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.Authentication;

import br.com.pvv.senai.model.DashboardResponse;
import br.com.pvv.senai.service.DashboardService;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class DashboardControllerTest {
	
	@InjectMocks 
	DashboardController controller;
	
	@Mock
	DashboardService service;
	
	@Test
	@DisplayName("OBTEM DADOS DASHBOARD - 200")
	void get_200() {
		Authentication auth = mock(Authentication.class);
		DashboardResponse response = mock(DashboardResponse.class);
		when(service.get(any(Authentication.class))).thenReturn(response);
		
		var dashboard = controller.get(auth);
		
		verify(service).get(any(Authentication.class));
	}

}
