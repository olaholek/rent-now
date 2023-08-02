package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

@Entity(name = "RATING")
@NoArgsConstructor
@Getter
@Setter
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RATING_ID", unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @LazyGroup("user")
    @NotNull
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Accommodation.class)
    @JoinColumn(name = "ACCOMMODATION_ID", referencedColumnName = "ACCOMMODATION_ID")
    @LazyGroup("accommodation")
    @NotNull
    private Accommodation accommodation;

    @Column(name = "GRADE", nullable = false)
    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    private Long grade;

    @Column(name = "DESCRIPTION")
    private String description;
}
