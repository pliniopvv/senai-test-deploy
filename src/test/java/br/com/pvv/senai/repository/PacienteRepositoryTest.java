package br.com.pvv.senai.repository;

import br.com.pvv.senai.entity.Paciente;
import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PacienteRepositoryTest {

    @Autowired
    PacienteRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    Usuario usuario;
    Paciente paciente;

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
    }

    @Test
    @DisplayName("Deve salvar paciente no repositório e retornar paciente salvo")
    void save() {
        Paciente pacienteSalvo = repository.save(paciente);
        assertTrue(repository.count() > 0);
        assertEquals(paciente.getName(), pacienteSalvo.getName());
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar salvar um paciente sem nome")
    void saveSemNome() {
        paciente.setName(null);
        assertThrows(Exception.class, () -> repository.save(paciente));
    }

    @Test
    @DisplayName("Deve retornar uma pessoa paciente pelo e-mail")
    void findByEmail() {
        repository.save(paciente);
        Paciente pacienteEncontrado = repository.findByEmail("paciente@mail.com");
        assertEquals(paciente.getName(), pacienteEncontrado.getName());
    }

    @Test
    @DisplayName("Deve retornar uma lista de todas as pessoas pacientes salvas")
    void findAll() {
        repository.save(paciente);
        assertTrue(repository.findAll().size() > 0);
        assertEquals(paciente.getBirthCity(), repository.findAll().get(0).getBirthCity());
    }

    @Test
    @DisplayName("Deve retornar uma pessoa paciente através do Id")
    void findById() {
        Paciente pacienteSalvo = repository.save(paciente);
        Optional<Paciente> pacienteEncontrado = repository.findById(pacienteSalvo.getId());
        assertTrue(pacienteEncontrado.isPresent());
        assertEquals(pacienteSalvo.getId(), pacienteEncontrado.get().getId());
        assertEquals(pacienteSalvo.getBirthCity(), pacienteEncontrado.get().getBirthCity());
    }

    @Test
    @DisplayName("Deve deletar uma pessoa paciente do repositório")
    void delete() {
        Paciente pacienteSalvo = repository.save(paciente);
        Paciente pacienteParaDeletar = repository.findById(pacienteSalvo.getId()).orElseThrow();
        repository.delete(pacienteParaDeletar);
        assertFalse(repository.existsById(pacienteSalvo.getId()));
    }

    @Test
    @DisplayName("Deve verificar se uma pessoa paciente existe pelo id")
    void existsById() {
        Paciente pacienteSalvo = repository.save(paciente);
        assertTrue(repository.existsById(pacienteSalvo.getId()));
    }


    @Test
    @DisplayName("Deve contar quantidade de pessoas pacientes cadastradas")
    void count() {
        repository.save(paciente);
        long quantidade = repository.count();
        assertTrue(quantidade > 0);
    }
}