package pl.holowinska.rentnowbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.holowinska.rentnowbackend.mappers.AccommodationMapper;
import pl.holowinska.rentnowbackend.mappers.AddressMapper;
import pl.holowinska.rentnowbackend.model.entities.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;
import pl.holowinska.rentnowbackend.repository.AccommodationRepository;
import pl.holowinska.rentnowbackend.repository.ConvenienceRepository;
import pl.holowinska.rentnowbackend.repository.UserRepository;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
@Transactional
public class AccommodationServiceImpl implements AccommodationService {

    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final ConvenienceRepository convenienceRepository;

    public AccommodationServiceImpl(UserRepository userRepository, AccommodationRepository accommodationRepository, ConvenienceRepository convenienceRepository) {
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
        this.convenienceRepository = convenienceRepository;
    }


    @Override
    public AccommodationRS addAccommodation(AccommodationRQ accommodationRQ) {

        User user = userRepository.findById(accommodationRQ.getUserUUID())
                .orElse(userRepository.save(new User(accommodationRQ.getUserUUID())));
        if (accommodationRQ.getPriceForDay().compareTo(BigDecimal.ZERO) < 0 ||
                (accommodationRQ.getSquareFootage() != null && accommodationRQ.getSquareFootage() <= 0)) {
            throw new IllegalArgumentException();
        }

        Address address = AddressMapper.mapToEntity(accommodationRQ.getAddressRQ());
        Accommodation accommodation = new Accommodation();
        accommodation.setUser(user);
        accommodation.setAddress(address);
        accommodation.setDescription(accommodationRQ.getDescription());
        accommodation.setSquareFootage(accommodationRQ.getSquareFootage());
        accommodation.setPriceForDay(accommodationRQ.getPriceForDay());
        Accommodation saved = accommodationRepository.save(accommodation);

        HashMap<ConvenienceType, BigDecimal> conveniences = accommodationRQ.getConveniences();
        if (conveniences != null) {
            for (ConvenienceType type : conveniences.keySet()) {
                Convenience convenience = new Convenience();
                ConvenienceId id = new ConvenienceId(saved, type);
                convenience.setPrice(conveniences.get(type));
                convenience.setId(id);
                convenienceRepository.save(convenience);
            }
        }

        return AccommodationMapper.mapToDto(saved, conveniences);
    }
}
