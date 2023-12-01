package pl.holowinska.rentnowbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;
import pl.holowinska.rentnowbackend.model.entities.FavouriteObject;
import pl.holowinska.rentnowbackend.model.entities.FavouriteObjectId;
import pl.holowinska.rentnowbackend.model.entities.User;
import pl.holowinska.rentnowbackend.repository.AccommodationRepository;
import pl.holowinska.rentnowbackend.repository.FavouriteObjectRepository;
import pl.holowinska.rentnowbackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FavouriteObjectServiceImpl implements FavouriteObjectService {

    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final FavouriteObjectRepository favouriteObjectRepository;

    public FavouriteObjectServiceImpl(UserRepository userRepository, AccommodationRepository accommodationRepository, FavouriteObjectRepository favouriteObjectRepository) {
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
        this.favouriteObjectRepository = favouriteObjectRepository;
    }

    @Override
    public void addToFavourites(String uuid, Long accommodationId) throws AccommodationNotFoundException {
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElse(userRepository.save(new User(UUID.fromString(uuid))));
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }
        FavouriteObjectId id = new FavouriteObjectId();
        id.setAccommodation(accommodation.get());
        id.setUser(user);
        FavouriteObject favouriteObject = new FavouriteObject();
        favouriteObject.setId(id);

        favouriteObjectRepository.save(favouriteObject);
    }

    @Override
    public void deleteFromFavourites(String uuid, Long accommodationId) throws AccommodationNotFoundException {
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElse(userRepository.save(new User(UUID.fromString(uuid))));
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }
        FavouriteObjectId id = new FavouriteObjectId();
        id.setAccommodation(accommodation.get());
        id.setUser(user);
        Optional<FavouriteObject> toDelete = favouriteObjectRepository.findById(id);
        toDelete.ifPresent(favouriteObjectRepository::delete);
    }

    @Override
    public List<Long> getFavouritesByUser(String uuid) {
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElse(userRepository.save(new User(UUID.fromString(uuid))));
        return favouriteObjectRepository.getFavouritesByUser(user.getId());
    }
}
