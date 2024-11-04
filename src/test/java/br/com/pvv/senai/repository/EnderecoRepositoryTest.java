package br.com.pvv.senai.repository;

import br.com.pvv.senai.entity.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnderecoRepositoryTest {

    @Autowired
    EnderecoRepository repository;

    Endereco endereco;

    @BeforeEach
    void setup(){
        endereco = new Endereco();
        endereco.setCEP("00.000-00");
        endereco.setCidade("Florianópolis/SC");
    }

    @Test
    @DisplayName("Deve salvar endereço no repositório e retornar endereço salvo")
    void save(){
        Endereco enderecoSalvo = repository.save(endereco);
        assertEquals(endereco.getCidade(), enderecoSalvo.getCidade());
    }

    @Test
    @DisplayName("Deve retornar uma lista de todos os endereços salvos")
    void findAll() {
        repository.save(endereco);
        assertTrue(repository.findAll().size() > 0);
        assertEquals(endereco.getCidade(), repository.findAll().get(0).getCidade());
    }

    @Test
    @DisplayName("Deve retornar um endereço através do Id")
    void findById() {
        Endereco enderecoSalvo = repository.save(endereco);
        Optional<Endereco> enderecoEncontrado = repository.findById(enderecoSalvo.getId());
        assertTrue(enderecoEncontrado.isPresent());
        assertEquals(enderecoSalvo.getId(), enderecoEncontrado.get().getId());
        assertEquals(enderecoSalvo.getCidade(), enderecoEncontrado.get().getCidade());
    }

    @Test
    @DisplayName("Deve deletar um endereço do repositório")
    void delete() {
        repository.save(endereco);
        repository.delete(endereco);
        assertFalse(repository.existsById(1L));
    }

    @Test
    @DisplayName("Deve verificar se um endereço existe pelo id")
    void existsById() {
        Endereco enderecoSalvo = repository.save(endereco);
        assertTrue(repository.existsById(enderecoSalvo.getId()));
    }

}