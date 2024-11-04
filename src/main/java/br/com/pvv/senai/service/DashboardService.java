package br.com.pvv.senai.service;

import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.model.DashboardResponse;
import br.com.pvv.senai.security.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private ExameService exameService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private UsuarioService usuarioService;

    public DashboardResponse get(Authentication authentication) {
        DashboardResponse dashboard = new DashboardResponse();
        dashboard.setQtdConsultas(consultaService.count());
        dashboard.setQtdExames(exameService.count());
        dashboard.setQtdPacientes(pacienteService.count());

        var perfil = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (perfil.contains(Perfil.ADMIN.scope())) {
            dashboard.setQtdUsuarios(usuarioService.count());
        } else {
            dashboard.setQtdUsuarios(null);
        }
        return dashboard;
    }

}
