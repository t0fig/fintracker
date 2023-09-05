package me.t0fig.fintracker;

import me.t0fig.fintracker.model.User;
import me.t0fig.fintracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FintrackerApplication {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public FintrackerApplication(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(FintrackerApplication.class, args);
    }

    @Bean
    CommandLineRunner defaultUserInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return (args -> {
            userRepository.save(new User("admin", "admin@fintracker.com",
                    passwordEncoder.encode("1234"), "ROLE_USER," + "ROLE_ADMIN"));
            userRepository.save(new User("test_user", "test_user@fintracker.com",
                    passwordEncoder.encode("2345"), "ROLE_USER"));
        });
    }

}
