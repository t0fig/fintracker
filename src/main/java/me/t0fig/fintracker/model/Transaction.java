package me.t0fig.fintracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.t0fig.fintracker.exception.IllegalTransactionAmountException;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String category;
    double amount;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    User owner;
    LocalDate commitDate = LocalDate.now();

    public Transaction(String category, double amount) {
        if (amount < 0)
            throw new IllegalTransactionAmountException();
        this.category = category;
        this.amount = amount;
    }
}
