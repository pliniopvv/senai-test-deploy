package br.com.pvv.senai.service;

import br.com.pvv.senai.entity.Endereco;
import br.com.pvv.senai.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EnderecoServiceTest {

    @Mock
    EnderecoRepository repository;

    @InjectMocks
    EnderecoService service;
    
    Endereco endereco;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCEP("00.000-00");
        endereco.setCidade("Florianópolis/SC");
    }

    @Test
    @DisplayName("Deve injetar o repositório")
    void getRepository() {
            assertNotNull(service.getRepository());
    }

    @Test
    @DisplayName("Deve criar um endereço")
    void create() {
        // Given
        when(repository.save(any(Endereco.class))).thenReturn(endereco);
        // When
        Endereco enderecoSalvo = service.create(endereco);
        // Then
        assertNotNull(enderecoSalvo);
        assertEquals(enderecoSalvo.getId(), endereco.getId());
        verify(repository).save(any(Endereco.class));
    }

    @Test
    @DisplayName("Deve alterar um endereço")
    void alter() {
        // Given
        when(repository.save(any(Endereco.class))).thenReturn(endereco);
        // When
        Endereco enderecoSalvo = service.alter(1L, endereco);
        // Then
        assertNotNull(enderecoSalvo);
        assertEquals(enderecoSalvo.getId(), endereco.getId());
        verify(repository).save(any(Endereco.class));
    }

    @Test
    @DisplayName("Deve retornar um endereço pelo seu id")
    void get(){
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(endereco));
        // When
        Endereco enderecoEncontrado = service.get(1L);
        // Then
        assertNotNull(enderecoEncontrado);
        assertEquals(endereco.getId(), enderecoEncontrado.getId());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar endereço pelo id")
    void delete(){
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(endereco));
        // When
        boolean deletado = service.delete(1L);
        // Then
        assertTrue(deletado);
        verify(repository).delete(endereco);
    }
    
}