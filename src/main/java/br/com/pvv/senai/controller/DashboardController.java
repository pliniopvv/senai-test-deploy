package br.com.pvv.senai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pvv.senai.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping
	@Operation(summary = "Obtém dados gerais", description = "Obtém um resumo geral dos dados do sistema.", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity get(Authentication authentication) {
		var dashboard = dashboardService.get(authentication);
		return ResponseEntity.ok(dashboard);
	}

}
