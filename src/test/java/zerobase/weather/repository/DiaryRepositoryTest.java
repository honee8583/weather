package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DiaryRepositoryTest {

    @Autowired
    DiaryRepository diaryRepository;

    @Test
    void testFindAllByDate() {
        // given
        String stringDate = "2022-10-22";
        LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        Diary diary = Diary.builder()
                .id(1)
                .weather("weather")
                .icon("icon")
                .temperature(10)
                .date(date)
                .text("text")
                .build();

        // when
        diaryRepository.save(diary);
        List<Diary> diaryList = diaryRepository.findAllByDate(date);
        Diary savedDiary = diaryList.get(0);

        // then
        assertEquals("weather", savedDiary.getWeather());
        assertEquals("icon", savedDiary.getIcon());
        assertEquals(10, savedDiary.getTemperature());
        assertEquals(date, savedDiary.getDate());
        assertEquals("text", savedDiary.getText());
    }

    @Test
    void testFindAllByDateBetween() {
        // given
        String stringDate = "2020-05-30";
        LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        Diary newDiary = Diary.builder()
                .id(1)
                .weather("weather")
                .icon("icon")
                .temperature(10)
                .date(date)
                .text("text")
                .build();

        // when
        String stringStartDate = "2020-05-05";
        String stringEndDate = "2020-06-05";
        LocalDate startDate = LocalDate.parse(stringStartDate, DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(stringEndDate, DateTimeFormatter.ISO_DATE);

        diaryRepository.save(newDiary);
        List<Diary> diaryList = diaryRepository.findAllByDateBetween(startDate, endDate);
        Diary savedDiary = diaryList.get(0);

        // then
        assertEquals("weather", savedDiary.getWeather());
        assertEquals("icon", savedDiary.getIcon());
        assertEquals(10, savedDiary.getTemperature());
        assertEquals(date, savedDiary.getDate());
        assertEquals("text", savedDiary.getText());
    }

    @Test
    void testGetFirstByDate() {
        // given
        String stringDate = "2020-05-30";
        LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        Diary newDiary = Diary.builder()
                .id(1)
                .weather("weather")
                .icon("icon")
                .temperature(10)
                .date(date)
                .text("text")
                .build();

        // when
        diaryRepository.save(newDiary);
        Diary savedDiary = diaryRepository.getFirstByDate(date);

        // then
        assertEquals("weather", savedDiary.getWeather());
        assertEquals("icon", savedDiary.getIcon());
        assertEquals(10, savedDiary.getTemperature());
        assertEquals(date, savedDiary.getDate());
        assertEquals("text", savedDiary.getText());
    }

    @Test
    void testDeleteAllByDate() {
        // given
        String stringDate = "2020-05-30";
        LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        Diary newDiary = Diary.builder()
                .id(1)
                .weather("weather")
                .icon("icon")
                .temperature(10)
                .date(date)
                .text("text")
                .build();

        // when
        diaryRepository.save(newDiary);
        diaryRepository.deleteAllByDate(date);
        List<Diary> diaryList = diaryRepository.findAllByDate(date);

        // then
//        assertNull(diaryList.get(0));
        assertTrue(CollectionUtils.isEmpty(diaryList));
    }
}