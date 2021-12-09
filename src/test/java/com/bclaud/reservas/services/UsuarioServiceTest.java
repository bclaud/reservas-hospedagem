package com.bclaud.reservas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bclaud.reservas.client.AvatarAPI;
import com.bclaud.reservas.client.LinkImagem;
import com.bclaud.reservas.domain.Endereco;
import com.bclaud.reservas.domain.Usuario;
import com.bclaud.reservas.exceptions.RecursoNaoEncontradoException;
import com.bclaud.reservas.exceptions.usuario.UsuarioExceptions;
import com.bclaud.reservas.repositories.UsuarioRepository;
import com.bclaud.reservas.services.dto.usuario.AtualizarUsuarioRequest;
import com.bclaud.reservas.services.dto.usuario.UsuarioRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    AvatarAPI avatarClient;

    @InjectMocks
    UsuarioService usuarioService;

    Usuario usuario = new Usuario();
    Endereco endereco = new Endereco();
    UsuarioRequest usuarioRequest = new UsuarioRequest();
    AtualizarUsuarioRequest atualizarUsuarioRequest = new AtualizarUsuarioRequest();
    Pageable page;

    @BeforeEach
    public void init() {
        usuario = Usuario.builder().id(1L).nome("test user").email("test@test.com").senha("123456").cpf("12345678912")
                .dataNascimento(LocalDate.of(1990, Month.JANUARY, 1)).endereco(endereco)
                .avatarURL("https://i.some-random-api.ml/fh0zJdniX4.jpg").build();

        usuarioRequest.setNome("test user");
        usuarioRequest.setSenha("123456");
        usuarioRequest.setCpf("12345678912");
        usuarioRequest.setDataNascimento(LocalDate.of(1990, Month.JANUARY, 1));
        usuarioRequest.setEndereco(endereco);
        usuarioRequest.setEmail("test@test.com");

        endereco = Endereco.builder().id(1L).cep("12345-123").logradouro("logradouro teste").numero("111")
                .complemento("complemento teste").bairro("bairro teste").cidade("cidade teste").estado("estado teste")
                .build();

        atualizarUsuarioRequest.setNome("test user atualizado");
        atualizarUsuarioRequest.setEmail("testAtualizado@test.com");
        atualizarUsuarioRequest.setSenha("1234567");
        atualizarUsuarioRequest.setDataNascimento(LocalDate.of(1991, Month.JANUARY, 1));
        atualizarUsuarioRequest.setEndereco(endereco);

        page = PageRequest.of(0, 10);
    }

    @Test
    void atualizarUsuario_RetornaUsuarioAtualizado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<Usuario>());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        assertEquals(usuario.getNome(), usuarioService.atualizarUsuario(1L, atualizarUsuarioRequest).getNome());

    }

    @Test
    void cadastrarUsuario_RetornaUsuarioCadastrado() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<Usuario>());
        when(avatarClient.getImagem()).thenReturn(new LinkImagem("https://i.some-random-api.ml/fh0zJdniX4.jpg"));

        assertEquals(usuario, usuarioService.cadastrarUsuario(usuarioRequest));
    }

    @Test
    void cadastrarUsuario_RetornaExcecaoEmailDuplicado() {
        List<Usuario> list = new ArrayList<Usuario>();
        list.add(usuario);
        when(usuarioRepository.findAll()).thenReturn(list);

        assertThrows(UsuarioExceptions.class, () -> usuarioService.cadastrarUsuario(usuarioRequest));
    }

    @Test
    void cadastrarUsuario_RetornaExcecaoCPFDuplicado() {
        List<Usuario> list = new ArrayList<Usuario>();
        list.add(usuario);
        when(usuarioRepository.findAll()).thenReturn(list);
        usuarioRequest.setEmail("test2@test.com");
        assertThrows(UsuarioExceptions.class, () -> usuarioService.cadastrarUsuario(usuarioRequest));
    }

    @Test
    void listarUsuarioPorCPF_RetornaUsuarioDoCPF() {
        List<Usuario> list = new ArrayList<Usuario>();
        list.add(usuario);
        when(usuarioRepository.findAll()).thenReturn(list);

        assertEquals(usuario.getNome(), usuarioService.listarUsuarioPorCPF("12345678912").getNome());
    }

    @Test
    void listarUsuarioPorCPF_RetornaExcecaoNaoEncontrado() {
        List<Usuario> list = new ArrayList<Usuario>();
        when(usuarioRepository.findAll()).thenReturn(list);

        assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.listarUsuarioPorCPF("12345678912"));
    }

    @Test
    void listarUsuarioPorId_RetornaUsuarioDoID() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertEquals(usuario.getNome(), usuarioService.listarUsuarioPorId(1L).getNome());
    }

    @Test
    void listarUsuarioPorId_RetornaExcecaoNaoEncontrado() {
        assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.listarUsuarioPorId(1L));
    }

    @Test
    void listarUsuarios_RetornaPageComConteudo() {
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        listaUsuario.add(usuario);
        when(usuarioRepository.findAll(page)).thenReturn(new PageImpl<Usuario>(listaUsuario, page, 1));

        assertEquals(true, usuarioService.listarUsuarios(page).hasContent());
    }
}
