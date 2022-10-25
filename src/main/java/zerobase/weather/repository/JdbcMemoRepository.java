package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired  // properties파일에서 가져올수 있게 autowired
    public JdbcMemoRepository(DataSource dataSource) {  // properties 파일에서 지정한 datasource정보
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Memo save(Memo memo) {
        String sql = "insert into Memo values(?,?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    public List<Memo> findAll() {
        String sql = "select * from Memo";
        return jdbcTemplate.query(sql, memoRowMapper());
    }

    public Optional<Memo> findById(int id) {
        String sql = "select * from Memo where id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }

    private RowMapper<Memo> memoRowMapper() {
        // RowMapper : ResultSet 형식을 domain객체에 맞게 매핑해주는 것
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );
    }

}
