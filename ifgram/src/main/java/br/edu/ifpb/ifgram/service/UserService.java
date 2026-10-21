package br.edu.ifpb.ifgram.service;

import br.edu.ifpb.ifgram.dto.UserRequest;
import br.edu.ifpb.ifgram.dto.UserResponse;
import br.edu.ifpb.ifgram.model.User;
import br.edu.ifpb.ifgram.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    // injeção de dependêcia pelo construtor
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserResponse cria(UserRequest request) throws Exception {
        // regra de negócios: o e-mail não pode repetir
        if (repository.existsByEmail(request.email())) {
            throw new Exception(request.email());
        }
        User salvo = repository.save(new User(request.nome(), request.email()));
        return UserResponse.from(salvo);
    }
    public List<UserResponse> buscarTodosUsuarios(){
        List<User> listaUsuarios = repository.findAll();
        List<UserResponse> listaUsuariosResponse = new ArrayList<>();
        for(User user : listaUsuarios) {
            UserResponse userReponse = new UserResponse(
                    user.getId(),
                    user.getNome(),
                    user.getEmail()
            );
            listaUsuariosResponse.add(userReponse);
        }
        return listaUsuariosResponse;
    }

    public UserResponse atualizarUsuario(UserRequest request) throws Exception{
        Optional<User> usuario = repository.findByEmail(request.email());
        if (usuario.isEmpty()){
            throw new Exception("usuário não existe na base de dados.");
        }
        User usuarioSalvo = usuario.get();
        usuarioSalvo.setNome(request.nome());
        repository.save(usuarioSalvo);

        return UserResponse.from(usuarioSalvo);
    }

    public  UserResponse deleteUsuario(UserRequest request) throws Exception{
        Optional<User> usuario = repository.findByEmail(request.email());
        if (usuario.isEmpty()) {
            throw new Exception("Usuário não encrontrado!");
        }

        User usuarioDeletar = usuario.get();
        usuarioDeletar.setNome(request.nome());
        repository.delete(usuarioDeletar);

        return UserResponse.from(usuarioDeletar);
    }
}