package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 저장", notes = "this is note")
    @PostMapping("/create/diary")
    public void createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text) {
        diaryService.createDiary(date, text);
    }

    @ApiOperation("선택한 날짜의 모든 일기데이터를 가져옵니다.")
    @GetMapping("/read/diary")
    public List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.readDiary(date);
    }

    @ApiOperation("선택한 기간중의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diaries")
    public List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                   @ApiParam(value = "조회할 기간의 시작 날", example = "2020-02-01") LocalDate startDate,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                   @ApiParam(value = "조회할 기간의 마지막 날", example = "2020-02-02") LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")
    public void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            @RequestBody String text) {
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    public void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }
}
