package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity(name = "FAVOURITE_OBJECT")
@NoArgsConstructor
@Getter
@Setter
public class FavouriteObject implements Serializable {

    @EmbeddedId
    private FavouriteObjectId id;
}
