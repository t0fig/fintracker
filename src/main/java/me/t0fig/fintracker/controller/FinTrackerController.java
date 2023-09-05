package me.t0fig.fintracker.controller;

import me.t0fig.fintracker.exception.TransactionNotFoundException;
import me.t0fig.fintracker.exception.UsernameAlreadyTakenException;
import me.t0fig.fintracker.model.Transaction;
import me.t0fig.fintracker.model.User;
import me.t0fig.fintracker.service.TransactionService;
import me.t0fig.fintracker.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FinTrackerController {
    UserService userService;
    TransactionService transactionService;
    PasswordEncoder passwordEncoder;

    public FinTrackerController(UserService userService, TransactionService transactionService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/transactions")
    ResponseEntity<List<Transaction>> getTransactions(Authentication authentication,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                                      @RequestParam(required = false) List<String> category,
                                                      @RequestParam(required = false) Double min,
                                                      @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(userService.getTransactionsOfDuringByCategoryByAmount(authentication.getName(), start, end, category, min, max));
    }

    @GetMapping("/transactions/{id}")
    ResponseEntity<Transaction> getTransaction(Authentication authentication, @PathVariable long id) {
        if (userService.getTransactionsOf(authentication.getName()).stream().noneMatch(t -> t.getId() == id))
            throw new TransactionNotFoundException();
        return ResponseEntity.ok(transactionService.get(id));
    }

    @PostMapping("/transactions")
    ResponseEntity<Long> addTransaction(Authentication authentication, @RequestBody Transaction transaction) {
        transaction.setOwner(userService.get(authentication.getName()));
        transactionService.add(transaction);
        return ResponseEntity.ok(transaction.getId());
    }

    @PutMapping("/transactions/{id}")
    ResponseEntity<Long> updateTransaction(Authentication authentication, @PathVariable long id, @RequestBody Transaction transaction) {
        if (userService.getTransactionsOf(authentication.getName()).stream().noneMatch(t -> t.getId() == id))
            throw new TransactionNotFoundException();
        transactionService.update(id, transaction);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/transactions/{id}")
    ResponseEntity<Long> deleteTransaction(Authentication authentication, @PathVariable long id) {
        userService.removeTransaction(authentication.getName(), id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/spending")
    ResponseEntity<Double> getSpending(Authentication authentication,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                       @RequestParam(required = false) List<String> category) {
        return ResponseEntity.ok(userService.getSpendingOfDuringByCategoryByAmount(authentication.getName(), start, end, category));
    }

    @GetMapping("/income")
    ResponseEntity<Double> getIncome(Authentication authentication,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                     @RequestParam(required = false) List<String> category) {
        return ResponseEntity.ok(userService.getIncomeOfDuringByCategoryByAmount(authentication.getName(), start, end, category));
    }

    @GetMapping("/balance")
    ResponseEntity<Double> getBalance(Authentication authentication) {
        return ResponseEntity.ok(userService.getBalanceOf(authentication.getName()));
    }

    @GetMapping("/categories")
    ResponseEntity<List<String>> getCategories(Authentication authentication) {
        return ResponseEntity.ok(userService.getSpendingCategoriesOf(authentication.getName()));
    }

    @GetMapping("/user")
    ResponseEntity<User> getUser(Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.ok(null);
        User user = userService.get(authentication.getName());
        user.setPassword("****");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user")
    ResponseEntity<Long> addUser(@RequestBody User user) {
        if (userService.exists(user.getUsername()))
            throw new UsernameAlreadyTakenException();
        user.setRole("ROLE_USER");
        userService.add(user);
        user.setPassword("****");
        return ResponseEntity.ok(user.getId());
    }

    @DeleteMapping("/user")
    ResponseEntity<Integer> deleteUser(Authentication authentication, String username) {
        if(!authentication.getName().equals("admin") && !authentication.getName().equals(username))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        userService.remove(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user")
    ResponseEntity<Integer> updateUser(Authentication authentication, String username, User user) {
        if(!authentication.getName().equals("admin") && userService.exists(user.getUsername())
                && !authentication.getName().equals(username))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if (!user.getUsername().equals(username) && userService.exists(user.getUsername()))
            throw new UsernameAlreadyTakenException();
        userService.update(username, user);
        return ResponseEntity.ok().build();
    }
}
