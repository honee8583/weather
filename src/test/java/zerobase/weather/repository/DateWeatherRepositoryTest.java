package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DateWeatherRepositoryTest {

    @Autowired
    DateWeatherRepository dateWeatherRepository;

    @Test
    void testFindAllByDate() {
        // given
        String stringDate = "2022-10-22";
        LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        DateWeather dateWeather = DateWeather.builder()
                .weather("weather")
                .icon("icon")
                .date(date)
                .temperature(10)
                .build();

        // when
        DateWeather savedDateWeather = dateWeatherRepository.save(dateWeather);
        List<DateWeather> dateWeatherList = dateWeatherRepository.findAllByDate(date);

        // then
        assertEquals("weather", dateWeatherList.get(0).getWeather());
        assertEquals("icon", dateWeatherList.get(0).getIcon());
        assertEquals(10, dateWeatherList.get(0).getTemperature());
        assertEquals(date, dateWeatherList.get(0).getDate());
    }
}