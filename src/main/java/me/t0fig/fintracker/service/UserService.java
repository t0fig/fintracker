package me.t0fig.fintracker.service;

import jakarta.persistence.EntityManager;
import me.t0fig.fintracker.model.Transaction;
import me.t0fig.fintracker.model.User;
import me.t0fig.fintracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserRepository userRepository;
    EntityManager entityManager;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User get(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public boolean exists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void update(String username, User user) {
        User oldUser = get(username);
        if (user.getEmail() != null)
            oldUser.setEmail(user.getEmail());
        if (user.getPassword() != null)
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(oldUser);
    }

    public void remove(String username) {
        userRepository.deleteByUsername(username);
    }

    public void removeTransaction(String username, long id) {
        User user = get(username);
        user.getTransactions().removeIf(t -> t.getId() == id);
        userRepository.save(user);
    }

    public List<Transaction> getTransactionsOf(String username) {
        return get(username).getTransactions();
    }

    public List<Transaction> getTransactionsOfDuringByCategoryByAmount(String username, LocalDate startDate,
                                                                       LocalDate endDate, List<String> category,
                                                                       Double minAmount, Double maxAmount) {
        return getTransactionsOf(username).stream().filter(t -> category == null || category.contains(t.getCategory()))
                .filter(t -> startDate == null || t.getCommitDate().isAfter(startDate))
                .filter(t -> endDate == null || t.getCommitDate().isBefore(endDate))
                .filter(t -> minAmount == null || t.getAmount() >= minAmount)
                .filter(t -> maxAmount == null || t.getAmount() <= maxAmount)
                .toList();
    }

    public double getSpendingOfDuringByCategoryByAmount(String username, LocalDate startDate, LocalDate endDate, List<String> category) {
        return getTransactionsOfDuringByCategoryByAmount(username, startDate, endDate, category, null, null).stream().filter(t -> !t.getCategory().equals("income"))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public double getIncomeOfDuringByCategoryByAmount(String username, LocalDate startDate, LocalDate endDate, List<String> category) {
        return getTransactionsOfDuringByCategoryByAmount(username, startDate, endDate, category, null, null).stream().filter(t -> t.getCategory().equals("income"))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public List<String> getSpendingCategoriesOf(String username) {
        return getTransactionsOf(username).stream().map(Transaction::getCategory).collect(Collectors.toSet())
                .stream().filter(c -> !c.equals("income")).toList();
    }

    public double getBalanceOf(String username) {
        return getIncomeOfDuringByCategoryByAmount(username, null, null, null)
                - getSpendingOfDuringByCategoryByAmount(username, null, null, null);
    }
}
