package br.com.pvv.senai.repository;

import br.com.pvv.senai.entity.Exame;
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
class ExameRepositoryTest {

    @Autowired
    ExameRepository repository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    UserRepository userRepository;

    Usuario usuario;
    Paciente paciente;
    Exame exame;

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

        exame = new Exame();
        exame.setPaciente(paciente);
        exame.setNome("Nome do exame");
        exame.setResultados("Resultado do exame");
        exame.setDataExame(LocalDate.parse("2021-10-10"));
        exame.setHoraExame(LocalTime.parse("10:00"));
        exame.setTipo("Tipo do exame");
        exame.setLaboratorio("Laboratório");
    }

    @Test
    @DisplayName("Deve salvar exame no repositório e retornar exame salvo")
    void save() {
        Exame exameSalvo = repository.save(exame);
        assertNotNull(exameSalvo);
        assertEquals(exame.getNome(), exameSalvo.getNome());
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar salvar um exame sem nome")
    void saveSemNome() {
        exame.setNome(null);
        assertThrows(Exception.class, () -> repository.save(exame));
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar salvar um exame sem data")
    void saveSemData() {
        exame.setDataExame(null);
        assertThrows(Exception.class, () -> repository.save(exame));
    }

    @Test
    @DisplayName("Deve retornar uma lista de exames através do Id da pessoa paciente")
    void findByPacienteId() {
        repository.save(exame);
        assertTrue(repository.findByPacienteId(paciente.getId()).size() > 0);
        assertEquals(exame.getNome(), repository.findByPacienteId(paciente.getId()).get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar uma lista de todos os exames no repositório")
    void findAll() {
        repository.save(exame);
        assertTrue(repository.findAll().size() > 0);
        assertEquals(exame.getNome(), repository.findAll().get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar um exame através do Id")
    void findById() {
        Exame exameSalvo = repository.save(exame);
        Optional<Exame> exameEncontrado = repository.findById(exameSalvo.getId());
        assertTrue(exameEncontrado.isPresent());
        assertEquals(exameSalvo.getId(), exameEncontrado.get().getId());
        assertEquals(exameSalvo.getNome(), exameEncontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve deletar um exame do repositório")
    void delete() {
        Exame exameSalvo = repository.save(exame);
        Exame exameParaDeletar = repository.findById(exameSalvo.getId()).orElseThrow();
        repository.delete(exameParaDeletar);
        assertFalse(repository.existsById(exameSalvo.getId()));
    }

    @Test
    @DisplayName("Deve verificar se um exame existe pelo id")
    void existsById() {
        Exame exameSalvo = repository.save(exame);
        assertTrue(repository.existsById(exameSalvo.getId()));
    }

    @Test
    @DisplayName("Deve contar quantidade de exames no sistema")
    void count() {
        repository.save(exame);
        long quantidade = repository.count();
        assertTrue(quantidade > 0);
    }

}