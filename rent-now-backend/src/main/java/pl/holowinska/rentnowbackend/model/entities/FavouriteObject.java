package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

@Entity(name = "FAVOURITE_OBJECT")
@NoArgsConstructor
@Getter
@Setter
public class FavouriteObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAVOURITE_OBJECT_ID", unique = true)
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
}
