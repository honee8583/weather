package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(WeatherApplication.class);

    /**
     * 매일 새벽 1시 날씨데이터 저장
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
        dateWeatherRepository.save(getWeatherFromApi());
    }

    /**
     * DB에서 오늘 날씨정보 가져오기(없을 경우 api 호출)
     */
    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherList = dateWeatherRepository.findAllByDate(date);
        if (CollectionUtils.isEmpty(dateWeatherList)) {
            // 새로 api에서 날씨정보를 가져와야 한다.
            return getWeatherFromApi();
        } else {
            return dateWeatherList.get(0);
        }
    }

    /**
     * 다이어리 생성
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {
        logger.info("Started to Create Diary");
        // 날씨데이터 가져오기 (DB)
        DateWeather dateWeather = getDateWeather(date);

        // 파싱된 데이 + 일기 값 db에 넣기
        Diary nowDiary = Diary.builder()
                .text(text)
                .build();
        nowDiary.setDateWeather(dateWeather);
        diaryRepository.save(nowDiary);
        logger.info("End to Create Diary");
    }

    /**
     * 해당 날짜의 다이어리 조회
     */
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
//        if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {
//            throw new InvalidDate();
//        }
        return diaryRepository.findAllByDate(date);
    }

    /**
     * 해당 날짜 사이의 모든 일기 조회
     */
    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    /**
     * 다이어리 수정
     */
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    /**
     * 해당 날짜의 모든 일기 삭제
     */
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    private DateWeather getWeatherFromApi() {
        // 날씨데이터 가져오기
        String weatherData = getWeatherString();
        // 받아온 날씨 json 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);  // temp, main, icon

        DateWeather dateWeather = DateWeather.builder()
                .date(LocalDate.now())
                .weather(parsedWeather.get("main").toString())
                .icon(parsedWeather.get("icon").toString())
                .temperature((Double) parsedWeather.get("temp"))
                .build();

        return dateWeather;
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // url을 http형식으로 연결
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {   // readLine() : 한줄을 읽는다(String 으로 반환)
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();   // json-simple
        JSONObject jsonObject;

        try{
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

}
