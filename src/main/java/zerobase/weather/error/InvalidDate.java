package zerobase.weather.error;

import org.apache.logging.log4j.message.Message;

public class InvalidDate extends RuntimeException{
    private static final String MESSAGE = "너무 과거 또는 미래의 날짜입니다.";

    public InvalidDate() {
        super(MESSAGE);
    }
}
