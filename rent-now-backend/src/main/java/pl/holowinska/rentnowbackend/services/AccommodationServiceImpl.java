package pl.holowinska.rentnowbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.mappers.AccommodationMapper;
import pl.holowinska.rentnowbackend.mappers.AddressMapper;
import pl.holowinska.rentnowbackend.model.entities.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;
import pl.holowinska.rentnowbackend.repository.AccommodationRepository;
import pl.holowinska.rentnowbackend.repository.ConvenienceRepository;
import pl.holowinska.rentnowbackend.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
                (accommodationRQ.getSquareFootage() != null && accommodationRQ.getSquareFootage().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException();
        }

        Address address = AddressMapper.mapToEntity(accommodationRQ.getAddressRQ());
        Accommodation accommodation = new Accommodation();
        accommodation.setUser(user);
        Accommodation saved = setFieldsAndSaveAccommodation(accommodationRQ, address, accommodation);

        HashMap<ConvenienceType, BigDecimal> conveniences = setAndSaveConveniences(accommodationRQ, saved);

        return AccommodationMapper.mapToDto(saved, conveniences);
    }

    @Override
    public AccommodationRS addPhotosToAccommodation(Long accommodationId, List<MultipartFile> files) throws IOException, AccommodationNotFoundException {
        File directory = new File("D:\\Praca Inżynierska\\photos\\" + accommodationId);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        directory.setWritable(true);
        for (MultipartFile file : files) {
            int i = file.getOriginalFilename().lastIndexOf(".");
            String ext = file.getOriginalFilename().substring(i);
            Path of1 = Path.of("D:\\Praca Inżynierska\\photos\\" + accommodationId + "\\" + new Random().nextLong() + ext);
            Files.write(of1, file.getBytes());
        }

        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }
        HashMap<ConvenienceType, BigDecimal> conveniences = getConveniences(accommodationId);
        return AccommodationMapper.mapToDto(accommodation.get(), conveniences);
    }

    @Override
    public AccommodationRS getAccommodationById(Long accommodationId) throws AccommodationNotFoundException {
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }
        HashMap<ConvenienceType, BigDecimal> conveniences = getConveniences(accommodationId);
        return AccommodationMapper.mapToDto(accommodation.get(), conveniences);
    }

    @Override
    public Page<AccommodationRS> getAccommodationByUserUUID(UUID userUUID, Pageable pageable) {
        return accommodationRepository.getAllByUser(userUUID, pageable)
                .map(a -> AccommodationMapper.mapToDto(a, getConveniences(a.getId())));
    }

    @Override
    public void deleteAccommodation(Long accommodationId) {
        convenienceRepository.deleteConveniencesByAccommodationId(accommodationId);
        accommodationRepository.deleteById(accommodationId);
    }

    @Override
    public AccommodationRS updateAccommodation(Long accommodationId, AccommodationRQ accommodationRQ) throws AccommodationNotFoundException {
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(accommodationId);
        if (optionalAccommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }
        Address address = AddressMapper.mapToEntity(accommodationRQ.getAddressRQ());
        Accommodation accommodation = optionalAccommodation.get();
        Accommodation saved = setFieldsAndSaveAccommodation(accommodationRQ, address, accommodation);

        convenienceRepository.deleteConveniencesByAccommodationId(accommodationId);
        HashMap<ConvenienceType, BigDecimal> conveniences = setAndSaveConveniences(accommodationRQ, saved);
        return AccommodationMapper.mapToDto(saved, conveniences);
    }

    private Accommodation setFieldsAndSaveAccommodation(AccommodationRQ accommodationRQ, Address address, Accommodation accommodation) {
        accommodation.setAddress(address);
        accommodation.setDescription(accommodationRQ.getDescription());
        accommodation.setSquareFootage(accommodationRQ.getSquareFootage());
        accommodation.setPriceForDay(accommodationRQ.getPriceForDay());
        accommodation.setMaxNoOfPeople(accommodationRQ.getMaxNoOfPeople());
        accommodation.setName(accommodationRQ.getName());
        return accommodationRepository.save(accommodation);
    }

    private HashMap<ConvenienceType, BigDecimal> getConveniences(Long accommodationId) {
        List<Convenience> convenienceList = convenienceRepository.getConvenienceByAccommodationId(accommodationId);
        HashMap<ConvenienceType, BigDecimal> conveniences = new HashMap<>();
        for (Convenience convenience : convenienceList) {
            conveniences.put(convenience.getId().getConvenienceType(), convenience.getPrice());
        }
        return conveniences;
    }

    private HashMap<ConvenienceType, BigDecimal> setAndSaveConveniences(AccommodationRQ accommodationRQ, Accommodation saved) {
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
        return conveniences;
    }
}
