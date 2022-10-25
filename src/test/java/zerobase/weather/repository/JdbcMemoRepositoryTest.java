package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 현재 클래스가 test 클래스임을 알림
@Transactional  // test 코드로 인해 데이터베이스 내용이 바뀌는것을 방지하기위한 어노테이션
class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {
        // given
        Memo newMemo = new Memo(2, "Insert Memo Test");

        // when
        jdbcMemoRepository.save(newMemo);

        // then
        Optional<Memo> result = jdbcMemoRepository.findById(2);
        assertEquals(result.get().getText(), "Insert Memo Test");
    }

    @Test
    void findAllMemoTest() {
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }
}