package pl.holowinska.rentnowbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.mappers.AccommodationMapper;
import pl.holowinska.rentnowbackend.model.entities.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;
import pl.holowinska.rentnowbackend.repository.AccommodationRepository;
import pl.holowinska.rentnowbackend.repository.ConvenienceRepository;
import pl.holowinska.rentnowbackend.repository.FavouriteObjectRepository;
import pl.holowinska.rentnowbackend.repository.UserRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FavouriteObjectServiceImpl implements FavouriteObjectService {

    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final FavouriteObjectRepository favouriteObjectRepository;
    private final ConvenienceRepository convenienceRepository;

    public FavouriteObjectServiceImpl(UserRepository userRepository, AccommodationRepository accommodationRepository, FavouriteObjectRepository favouriteObjectRepository, ConvenienceRepository convenienceRepository) {
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
        this.favouriteObjectRepository = favouriteObjectRepository;
        this.convenienceRepository = convenienceRepository;
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
    public List<Long> getFavouriteListByUser(String uuid) {
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElse(userRepository.save(new User(UUID.fromString(uuid))));
        return favouriteObjectRepository.getFavouritesByUser(user.getId());
    }

    @Override
    public Page<AccommodationRS> getFavouritesByUser(String uuid, Pageable pageable) {
        return favouriteObjectRepository.getAllByUser(UUID.fromString(uuid), pageable)
                .map(a -> AccommodationMapper.mapToDto(a, getConveniences(a.getId())));
    }

    private HashMap<ConvenienceType, BigDecimal> getConveniences(Long accommodationId) {
        List<Convenience> convenienceList = convenienceRepository.getConvenienceByAccommodationId(accommodationId);
        HashMap<ConvenienceType, BigDecimal> conveniences = new HashMap<>();
        for (Convenience convenience : convenienceList) {
            conveniences.put(convenience.getId().getConvenienceType(), convenience.getPrice());
        }
        return conveniences;
    }
}
