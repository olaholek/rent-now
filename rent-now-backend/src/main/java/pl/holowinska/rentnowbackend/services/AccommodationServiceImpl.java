package pl.holowinska.rentnowbackend.services;

import jakarta.persistence.criteria.Predicate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.mappers.AccommodationMapper;
import pl.holowinska.rentnowbackend.mappers.AddressMapper;
import pl.holowinska.rentnowbackend.model.entities.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.rq.AccommodationCriteriaRQ;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;
import pl.holowinska.rentnowbackend.repository.AccommodationRepository;
import pl.holowinska.rentnowbackend.repository.BookingRepository;
import pl.holowinska.rentnowbackend.repository.ConvenienceRepository;
import pl.holowinska.rentnowbackend.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccommodationServiceImpl implements AccommodationService {

    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final ConvenienceRepository convenienceRepository;
    private final BookingRepository bookingRepository;

    public AccommodationServiceImpl(UserRepository userRepository, AccommodationRepository accommodationRepository, ConvenienceRepository convenienceRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
        this.convenienceRepository = convenienceRepository;
        this.bookingRepository = bookingRepository;
    }


    @Override
    public AccommodationRS addAccommodation(AccommodationRQ accommodationRQ) {

        User user = userRepository.findById(accommodationRQ.getUserUUID())
                .orElse(userRepository.save(new User(accommodationRQ.getUserUUID())));
        if (accommodationRQ.getPriceForDay().compareTo(BigDecimal.ZERO) < 0
                || (accommodationRQ.getSquareFootage() != null && accommodationRQ.getSquareFootage().compareTo(BigDecimal.ZERO) <= 0)) {
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
        return accommodationRepository.getAllByUser(userUUID, pageable).map(a -> AccommodationMapper.mapToDto(a, getConveniences(a.getId())));
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

    @Override
    public Page<AccommodationRS> getAccommodationListByFilter(AccommodationCriteriaRQ accommodationCriteriaRQ, Pageable pageable) {
        if (accommodationCriteriaRQ.getStartDate().isBefore(LocalDate.now()) || !accommodationCriteriaRQ.getEndDate().isAfter(accommodationCriteriaRQ.getStartDate())) {
            throw new IllegalArgumentException();
        }
        return accommodationRepository.findAll(accommodationByCriteria(accommodationCriteriaRQ), pageable)
                .map(entity -> AccommodationMapper.mapToDto(entity, getConveniences(entity.getId())));
    }

    @Override
    public List<InputStreamResource> getAccommodationPhotos(Long accommodationId) throws AccommodationNotFoundException, IOException {

        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }

        List<InputStreamResource> photos = new ArrayList<>();
        File directory = new File("D:\\Praca Inżynierska\\photos\\" + accommodationId);

        if (directory.exists() && directory.isDirectory()) {
            File[] fileArray = directory.listFiles();
            if (fileArray != null) {
                for (File file : fileArray) {
                    photos.add(new InputStreamResource(new FileInputStream(file)));
                }
            }
        }
        return photos;
    }

    @Override
    public InputStreamResource getAccommodationMainPhoto(Long accommodationId) throws AccommodationNotFoundException, IOException {

        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }

        File defaultImage = new File("D:\\Praca Inżynierska\\photos\\default");
        InputStreamResource mainPhoto = null;
        File directory = new File("D:\\Praca Inżynierska\\photos\\" + accommodationId);

        if (directory.exists() && directory.isDirectory()) {
            File[] fileArray = directory.listFiles();
            if (fileArray != null && fileArray.length != 0) {
                mainPhoto = new InputStreamResource(new FileInputStream(fileArray[0]));
            }
        } else {
            File[] defaultArray = defaultImage.listFiles();
            if (defaultArray != null && defaultArray.length != 0) {
                mainPhoto = new InputStreamResource(new FileInputStream(defaultArray[0]));
            }
        }
        return mainPhoto;
    }

    private Specification<Accommodation> accommodationByCriteria(AccommodationCriteriaRQ accommodationCriteriaRQ) {
        return (root, query, cb) -> {
            long numberOfDays = getNumberOfBookingDays(accommodationCriteriaRQ.getStartDate(), accommodationCriteriaRQ.getEndDate());
            List<Long> accommodationIdsFromBooking = bookingRepository.getBookingAccommodationIdByDates
                    (accommodationCriteriaRQ.getStartDate(), accommodationCriteriaRQ.getEndDate());
            List<Long> allAccommodationIds = accommodationRepository.findAll().stream().map(Accommodation::getId).toList();
            List<Long> resultList = allAccommodationIds.stream().filter(element -> !accommodationIdsFromBooking.contains(element))
                    .collect(Collectors.toList());
            Predicate spec = root.get("id").in(resultList);
            if (accommodationCriteriaRQ.getCity() != null && !accommodationCriteriaRQ.getCity().isEmpty()) {
                spec = cb.and(spec, cb.like(root.get("address").get("city"), accommodationCriteriaRQ.getCity()));
            }
            if (accommodationCriteriaRQ.getStreet() != null && !accommodationCriteriaRQ.getStreet().isEmpty()) {
                spec = cb.and(spec, cb.like(root.get("address").get("street"), accommodationCriteriaRQ.getStreet()));
            }
            if (accommodationCriteriaRQ.getSquareFootage() != null) {
                spec = cb.and(spec, cb.equal(root.get("squareFootage"), accommodationCriteriaRQ.getSquareFootage()));
            }
            if (accommodationCriteriaRQ.getMinPrice() != null) {
                spec = cb.and(spec, cb.greaterThanOrEqualTo(root.get("priceForDay"), accommodationCriteriaRQ.getMinPrice()
                        .divide(BigDecimal.valueOf(numberOfDays), 2, RoundingMode.DOWN)));
            }
            if (accommodationCriteriaRQ.getMaxPrice() != null) {
                spec = cb.and(spec, cb.lessThanOrEqualTo(root.get("priceForDay"), accommodationCriteriaRQ.getMaxPrice()
                        .divide(BigDecimal.valueOf(numberOfDays), 2, RoundingMode.UP)));
            }
            if (accommodationCriteriaRQ.getMaxNoOfPeople() != null) {
                spec = cb.and(spec, cb.lessThanOrEqualTo(root.get("maxNoOfPeople"), accommodationCriteriaRQ.getMaxNoOfPeople()));
            }
            if (accommodationCriteriaRQ.getName() != null && !accommodationCriteriaRQ.getName().isEmpty()) {
                spec = cb.and(spec, cb.like(root.get("name"), accommodationCriteriaRQ.getName()));
            }
            List<ConvenienceType> filterConveniences = accommodationCriteriaRQ.getConveniences();
            if (filterConveniences != null && !filterConveniences.isEmpty()) {
                spec = cb.and(spec, root.get("id").in(convenienceRepository.getAccommodationByConveniencesList(filterConveniences,
                        filterConveniences.size())));
            }
            return spec;
        };
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

    private Long getNumberOfBookingDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
