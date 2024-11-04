package br.com.pvv.senai.repository;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    Usuario usuario = new Usuario();

    @BeforeEach
    void setup(){
        usuario.setEmail("usuario@teste.com");
        usuario.setPassword("12345678");
        usuario.setPerfil(Perfil.ADMIN);
    }

    @AfterEach
    void clear(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar pessoa usuária no repositório e retornar pessoa salva")
    void save(){
        Usuario usuarioSalvo = userRepository.save(usuario);
        assertEquals(usuario.getEmail(), usuarioSalvo.getEmail());
    }

    @Test
    @DisplayName("Deve verificar se uma pessoa usuária existe")
    void exists(){
        Usuario usuarioSalvo = userRepository.save(usuario);
        Example<Usuario> example = Example.of(usuarioSalvo);
        assertTrue(userRepository.exists(example));
    }

    @Test
    @DisplayName("Deve retornar uma pessoa usuária através do e-mail")
    void findByEmail() {
        userRepository.save(usuario);
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail("usuario@teste.com");
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals(usuario.getEmail(), usuarioEncontrado.get().getEmail());
    }


    @Test
    @DisplayName("Deve retornar uma lista de todas pessoas usuárias")
    void findAll() {
        userRepository.saveAndFlush(usuario);
        assertTrue(userRepository.findAll().size() > 0);
    }

    @Test
    @DisplayName("Deve retornar uma pessoa usuária através do Id")
    void findById() {
        Usuario usuarioSalvo = userRepository.save(usuario);
        Optional<Usuario> usuarioEncontrado = userRepository.findById(usuarioSalvo.getId());
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals(usuarioSalvo.getId(), usuarioEncontrado.get().getId());
        assertEquals(usuarioSalvo.getEmail(), usuarioEncontrado.get().getEmail());
    }

    @Test
    @DisplayName("Deve verificar se uma pessoa usuária existe através do Id")
    void existsById(){
        Usuario usuarioSalvo = userRepository.save(usuario);
        assertTrue(userRepository.existsById(usuarioSalvo.getId()));
    }

    @Test
    @DisplayName("Deve deletar uma pessoa usuária")
    void delete() {
        userRepository.save(usuario);
        userRepository.delete(usuario);
        assertFalse(userRepository.existsById(1L));
    }

    @Test
    @DisplayName("Deve contar quantidade de pessoa usuárias")
    void count() {
        userRepository.save(usuario);
        long quantidade = userRepository.count();
        assertTrue(quantidade > 0);
    }

}