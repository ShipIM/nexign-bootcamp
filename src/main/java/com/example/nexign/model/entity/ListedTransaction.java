package com.example.nexign.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "listed_transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "call_start")
    private Long start;

    @Column(name = "call_end")
    private Long end;

    private Short type;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
