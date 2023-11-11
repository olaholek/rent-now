package pl.holowinska.rentnowbackend.jobs;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.holowinska.rentnowbackend.repository.BookingRepository;

@Component
@Slf4j
public class UpdateBookingStatusJob implements Job {

    private final BookingRepository bookingRepository;

    public UpdateBookingStatusJob(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public JobStatus executeJob(JobContext jobContext) {
        bookingRepository.updateStartingReservations();
        bookingRepository.updateFinishedReservations();
        return null;
    }
}
