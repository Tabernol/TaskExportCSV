package com.krasnopolskyi.krasnopolskyiexportcsv.repository;

import com.krasnopolskyi.krasnopolskyiexportcsv.entity.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.List;

/**
 * Repository class for managing the persistence of goods in the database.
 * Uses Spring's JdbcTemplate for batch inserts.
 */
@Repository
@Slf4j
public class GoodsRepository {

    private static final String INSERT_GOODS = "INSERT IGNORE INTO krasnopolskyi_kzvo.table_goods (id, name, price, type_of_goods) " +
            "VALUES (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GoodsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a list of goods into the database in a batch.
     *
     * @param goods List of Goods objects to be inserted.
     * @return The total number of rows affected by the batch update.
     */
    public Integer insertRows(List<Goods> goods) {
        List<Object[]> args = goods.stream().map(item -> new Object[]{
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getTypeOfGoods().number})
                .toList();
        try {

            int[] updateRows = jdbcTemplate.batchUpdate(INSERT_GOODS, args);
            log.info("Repository inserting " + Arrays.stream(updateRows).sum() + " items into the database.");
            return Arrays.stream(updateRows).sum();
        }
        catch (DataAccessException e) {
            log.error("Error inserting rows into the database.", e.getCause());
            return 0;
        }
    }
}
