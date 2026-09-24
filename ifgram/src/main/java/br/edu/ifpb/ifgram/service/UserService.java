@Service
public class UserService {

    private final UserRepository repository;

    // injeção de dependência pelo construtor
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserResponse criar(UserRequest request) {
        // regra de negócio: o e-mail não pode repetir
        if (repository.existsByEmail(request.email())) {
            throw new EmailDuplicadoException(request.email());
        }
        User salvo = repository.save(new User(request.nome(), request.email()));
        return UserResponse.from(salvo);
    }
}