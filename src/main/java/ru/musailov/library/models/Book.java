package ru.musailov.library.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Column(name = "name")
    private String name;


    @NotEmpty(message = "Author name should not be empty")
    @Column(name = "author")
    private String author;

    @Min(value = 1000, message = "Дата написания книги должна быть больше 1000 года")
    @Column(name = "year")
    private Integer year;


    @ManyToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "id")
    @JsonBackReference
    private Reader owner;


    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Transient
    private boolean expired;

    public boolean isExpired() {
        if (takenAt == null) return false;
        return TimeUnit.MILLISECONDS.
                toDays((new Date().getTime()) - this.getTakenAt().getTime()) > 10;
    }

    public Book(String name, String author, int year) {
        this.name = name;
        this.author = author;
        this.year = year;
    }
}
