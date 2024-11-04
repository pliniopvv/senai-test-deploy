package br.com.pvv.senai.repository;

import br.com.pvv.senai.entity.Consulta;
import br.com.pvv.senai.entity.Paciente;
import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultaRepositoryTest {
    
    @Autowired
    ConsultaRepository repository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    UserRepository userRepository;

    Usuario usuario;
    Paciente paciente;
    Consulta consulta;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setEmail("usuario@teste.com");
        usuario.setTelefone("123456789");
        usuario.setEmail("joao.silva@example.com");
        usuario.setCpf("123.456.789-00");
        usuario.setPassword("senha123");
        usuario.setSenhaMascarada("****");
        usuario.setPerfil(Perfil.ADMIN);

        userRepository.save(usuario);

        paciente = new Paciente();
        paciente.setName("Nome de paciente");
        paciente.setCPF("000.000.000-00");
        paciente.setEmail("paciente@mail.com");
        paciente.setBirthCity("Cidade de nascimento");
        paciente.setBirthDate(Date.valueOf("1980-10-10"));
        paciente.setAllergies("Alergias");
        paciente.setGender("Gênero da pessoa paciente");
        paciente.setMaritalStatus("Estado civil");
        paciente.setEmergencyContact("(00) 00000-0000");
        paciente.setPhone("(00) 00000-0000");
        paciente.setUsuario(usuario);

        pacienteRepository.save(paciente);

        consulta = new Consulta();
        consulta.setPatient(paciente);
        consulta.setReason("Motivo da consulta");
        consulta.setIssueDescription("Descrição do problema");
        consulta.setDate(LocalDate.parse("2021-10-10"));
        consulta.setTime(LocalTime.parse("10:00"));
        consulta.setObservation("Observações");
        consulta.setPrescribedMedication("Medicação receitada");
    }

    @Test
    @DisplayName("Deve salvar consulta no repositório e retornar consulta salva")
    void save() {
        Consulta consultaSalva = repository.save(consulta);
        assertNotNull(consultaSalva);
        assertEquals(consulta.getReason(), consultaSalva.getReason());
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar salvar uma consulta sem o campo motivo da consulta")
    void saveSemMotivo() {
        consulta.setReason(null);
        assertThrows(Exception.class, () -> repository.save(consulta));
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar salvar uma consulta sem data")
    void saveSemData() {
        consulta.setDate(null);
        assertThrows(Exception.class, () -> repository.save(consulta));
    }

    @Test
    @DisplayName("Deve retornar uma lista de consultas através do Id da pessoa paciente")
    void findByPacienteId() {
        repository.save(consulta);
        assertTrue(repository.findByPatientId(paciente.getId()).size() > 0);
        assertEquals(consulta.getReason(), repository.findByPatientId(paciente.getId()).get(0).getReason());
    }

    @Test
    @DisplayName("Deve retornar uma lista de todas as consultas no repositório")
    void findAll() {
        repository.save(consulta);
        assertTrue(repository.findAll().size() > 0);
        assertEquals(consulta.getReason(), repository.findAll().get(0).getReason());
    }

    @Test
    @DisplayName("Deve retornar uma consulta através do Id")
    void findById() {
        Consulta consultaSalva = repository.save(consulta);
        Optional<Consulta> consultaEncontrada = repository.findById(consultaSalva.getId());
        assertTrue(consultaEncontrada.isPresent());
        assertEquals(consultaSalva.getId(), consultaEncontrada.get().getId());
        assertEquals(consultaSalva.getReason(), consultaEncontrada.get().getReason());
    }

    @Test
    @DisplayName("Deve deletar uma consulta do repositório")
    void delete() {
        Consulta consultaSalva = repository.save(consulta);
        Consulta consultaParaDeletar = repository.findById(consultaSalva.getId()).orElseThrow();
        repository.delete(consultaParaDeletar);
        assertFalse(repository.existsById(consultaSalva.getId()));
    }

    @Test
    @DisplayName("Deve verificar se uma consulta existe pelo id")
    void existsById() {
        Consulta consultaSalva = repository.save(consulta);
        assertTrue(repository.existsById(consultaSalva.getId()));
    }

    @Test
    @DisplayName("Deve contar quantidade de consultas no sistema")
    void count() {
        repository.save(consulta);
        long quantidade = repository.count();
        assertTrue(quantidade > 0);
    }
    

}