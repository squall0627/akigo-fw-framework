package com.akigo;

import com.akigo.dao.datasource.DataSource;
import com.akigo.dao.datasource.nosql.DefaultNoSQLDataSource;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        DataSource ds = new DefaultNoSQLDataSource(null);
        ds.getDaoBuilder()
                .withSearch(cs -> cs
                        .where(null, null, null)
                        .groupBy(null)
                        .having(null, null)
                        .orderBy(null)


                );
        ds.getDaoBuilder()
                .withSearch(cs -> cs
                        .where(aos -> aos
                                .and(null, null, null)
                                .and(aos2 -> aos2
                                        .and(null, null, null)
                                        .or(null, null, null)
                                )
                        )
                ).getResultList();

        ds.getDaoBuilder().withCreate(cs -> cs.notify()).create();
        ds.getDaoBuilder().withUpdate(cs -> cs.where(cs2 -> cs.where(null))).update();
        ds.getDaoBuilder().withDelete(cs -> cs.where(null)).delete();
    }
}
