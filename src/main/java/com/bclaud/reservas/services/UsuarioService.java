package com.bclaud.reservas.services;

import com.bclaud.reservas.client.AvatarAPI;
import com.bclaud.reservas.domain.Usuario;
import com.bclaud.reservas.exceptions.RecursoNaoEncontradoException;
import com.bclaud.reservas.exceptions.usuario.UsuarioExceptions;
import com.bclaud.reservas.repositories.UsuarioRepository;
import com.bclaud.reservas.services.dto.usuario.AtualizarUsuarioRequest;
import com.bclaud.reservas.services.dto.usuario.UsuarioRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    AvatarAPI avatarClient;

    public Usuario cadastrarUsuario(UsuarioRequest request) throws UsuarioExceptions {
        if (isDuplicatedByEmail(request.getEmail())) {
            throw new UsuarioExceptions(
                    "Já existe um recurso do tipo Usuario com E-Mail com o valor '" + request.getEmail() + "''.");
        }
        if (isDuplicatedByCPF(request.getCpf())) {
            throw new UsuarioExceptions(
                    "Já existe um recurso do tipo Usuario com CPF com o valor '" + request.getCpf() + "'.");
        }

        Usuario usuario = requestToUsuario(request);
        usuario.setAvatarURL(pegarImagemAvatar());

        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> listarUsuarios(Pageable page) {
        return usuarioRepository.findAll(page);
    }

    public Usuario listarUsuarioPorId(Long id) throws RecursoNaoEncontradoException {
        return usuarioRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(
                "Nenhum(a) Usuario com Id com o valor '" + id + "' foi encontrado."));
    }

    public Usuario listarUsuarioPorCPF(String cpf) {
        return usuarioRepository.findAll().stream().filter(u -> u.getCpf().equals(cpf)).findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nenhum(a) Usuario com CPF com o valor '" + cpf + "' foi encontrado."));
    }

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioRequest request) throws UsuarioExceptions {

        Usuario usuarioBanco = listarUsuarioPorId(id);
        if (isDuplicatedByEmail(request.getEmail()) && !usuarioBanco.getEmail().equals(request.getEmail())) {
            throw new UsuarioExceptions(
                    "Já existe um recurso do tipo Usuario com E-Mail com o valor '" + request.getEmail() + "''.");
        }
        Usuario usuarioAtualizado = requestAtualizarToUsuario(request);

        if (usuarioAtualizado.getEndereco() != null) {
            usuarioAtualizado.getEndereco().setId(usuarioBanco.getEndereco().getId());
        } else {
            usuarioAtualizado.setEndereco(usuarioBanco.getEndereco());
        }

        usuarioAtualizado.setId(id);
        usuarioAtualizado.setAvatarURL(usuarioBanco.getAvatarURL());
        usuarioAtualizado.setCpf(usuarioBanco.getCpf());

        return usuarioRepository.save(usuarioAtualizado);
    }

    private String pegarImagemAvatar() {
        return avatarClient.getImagem().getLink();
    }

    private boolean isDuplicatedByEmail(String email) {
        return usuarioRepository.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    private boolean isDuplicatedByCPF(String cpf) {
        return usuarioRepository.findAll().stream().anyMatch(u -> u.getCpf().equalsIgnoreCase(cpf));
    }

    private Usuario requestToUsuario(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());

        usuario.setEmail(request.getEmail());
        usuario.setCpf(request.getCpf());
        usuario.setDataNascimento(request.getDataNascimento());
        if (request.getEndereco() != null) {
            usuario.setEndereco(request.getEndereco());
        }
        usuario.setSenha(request.getSenha());
        return usuario;
    }

    private Usuario requestAtualizarToUsuario(AtualizarUsuarioRequest request) {

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());

        usuario.setEmail(request.getEmail());
        usuario.setDataNascimento(request.getDataNascimento());
        if (request.getEndereco() != null) {
            usuario.setEndereco(request.getEndereco());
        }
        usuario.setSenha(request.getSenha());
        return usuario;
    }
}
