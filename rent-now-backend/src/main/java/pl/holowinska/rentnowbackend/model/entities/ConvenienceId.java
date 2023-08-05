package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.LazyGroup;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.io.Serializable;

@Embeddable
public class ConvenienceId implements Serializable {

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Accommodation.class)
    @JoinColumn(name = "ACCOMMODATION_ID", referencedColumnName = "ACCOMMODATION_ID")
    @LazyGroup("accommodation")
    @NotNull
    private Accommodation accommodation;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "CONVENIENCE_TYPE")
    @NotNull
    private ConvenienceType convenienceType;
}
