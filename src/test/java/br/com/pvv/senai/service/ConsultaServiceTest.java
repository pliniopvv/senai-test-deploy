package br.com.pvv.senai.service;

import br.com.pvv.senai.entity.Consulta;
import br.com.pvv.senai.entity.Paciente;
import br.com.pvv.senai.repository.ConsultaRepository;
import br.com.pvv.senai.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsultaServiceTest {

    @Mock
    ConsultaRepository repository;

    @Mock
    PacienteRepository pacienteRepository;

    @InjectMocks
    ConsultaService service;

    @Mock
    PacienteService pacienteService;

    Consulta consulta;
    Paciente paciente;
    List<Consulta> consultas;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setEmail("paciente@teste.com");
        paciente.setName("João Silva");
        paciente.setGender("Masculino");
        paciente.setCPF("123.456.789-00");
        paciente.setRG("SC-12.345.678");
        paciente.setMaritalStatus("Solteiro");
        paciente.setPhone("(34) 9 8765-4321");
        paciente.setBirthCity("Florianópolis");
        paciente.setEmergencyContact("(34) 9 8765-4321");
        paciente.setAllergies("Nenhuma");
        paciente.setSpecialCare("Nenhum");
        paciente.setInsuranceCompany("Saúde Qcode");
        paciente.setInsuranceNumber("1234567890");

        consulta = new Consulta();
        consulta.setId(1L);
        consulta.setReason("Razão da consulta");
        consulta.setObservation("Observações");
        consulta.setPatient(paciente);
        consulta.setDate(LocalDate.parse("2021-12-31"));
        consulta.setTime(LocalTime.parse("10:00:00"));
    }


    @Test
    @DisplayName("Deve injetar o repositório")
    void getRepository() {
        assertNotNull(service.getRepository());
    }

    @Test
    @DisplayName("Deve criar uma consulta")
    void create() {
        // Given
        when(repository.save(any(Consulta.class))).thenReturn(consulta);
        // When
        Consulta consultaSalva = service.create(consulta);
        // Then
        assertNotNull(consultaSalva);
        assertEquals(consultaSalva.getId(), consulta.getId());
        verify(repository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Deve alterar uma consulta")
    void alter() {
        // Given
        when(repository.save(any(Consulta.class))).thenReturn(consulta);
        // When
        Consulta consultaSalva = service.alter(1L, consulta);
        // Then
        assertNotNull(consultaSalva);
        assertEquals(consultaSalva.getId(), consulta.getId());
        verify(repository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Deve buscar consultas por id do paciente")
    void findByPacienteId() {
        // Given
        when(repository.findByPatientId(1L)).thenReturn(List.of(consulta));
        // When
        consultas = service.findByPacienteId(1L);
        // Then
        assertEquals(consultas.get(0).getId(), consulta.getId());
        verify(repository).findByPatientId(1L);
    }

    @Test
    @DisplayName("Deve retornar consulta pelo id")
    void get(){
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(consulta));
        // When
        Consulta consultaEncontrada = service.get(1L);
        // Then
        assertNotNull(consultaEncontrada);
        assertEquals(consulta.getId(), consultaEncontrada.getId());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar consulta pelo id")
    void delete(){
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(consulta));
        // When
        boolean deletado = service.delete(1L);
        // Then
        assertTrue(deletado);
        verify(repository).delete(consulta);
    }

    @Test
    @DisplayName("Deve retornar página de consultas")
    void paged(){
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Consulta> page = new PageImpl<>(List.of(consulta), pageable, 1);
        when(repository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);
        // When
        Page<Consulta> resultado = service.paged(Example.of(consulta), pageable);
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(consulta.getId(), resultado.getContent().get(0).getId());
        verify(repository).findAll(any(Example.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar lista com todas as consultas")
    void all(){
        // Given
        when(repository.findAll()).thenReturn(List.of(consulta));
        // When
        var resultado = service.all();
        // Then
        assertNotNull(resultado);
        assertEquals(paciente.getId(), resultado.get(0).getId());
        verify(repository).findAll();
    }


    @Test
    @DisplayName("Deve retornar quantidade de consultas no repositório")
    void count() {
        // Given
        when(repository.count()).thenReturn(1L);
        // When
        Long quantidade = service.count();
        // Then
        assertEquals(1L, quantidade);
        verify(repository).count();
    }


}