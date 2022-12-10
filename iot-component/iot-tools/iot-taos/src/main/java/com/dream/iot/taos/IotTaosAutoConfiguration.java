package com.dream.iot.taos;

import com.dream.iot.taos.proxy.TaosHandleProxyMatcher;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * create time: 2022/1/18
 *
 * @author dream
 * @since 1.0
 */
@Configuration
@EnableScheduling
public class IotTaosAutoConfiguration {

    @Configuration
    @AutoConfigureAfter(DataSourceAutoConfiguration.class)
    public static class DataSourceConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "jdbcTemplate")
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

    @Bean
    @ConditionalOnMissingBean(TaosSqlManager.class)
    public TaosSqlManager taosSqlManager() {
        return new DefaultTaosSqlManager();
    }

    @Bean
    @ConditionalOnMissingBean(TaosSqlContextScheduler.class)
    public TaosSqlContextScheduler taosSqlContextScheduler() {
        return new DefaultSqlContextScheduler();
    }

    @Bean
    @Order(10000)
    @ConditionalOnBean(TaosHandle.class)
    @ConditionalOnMissingBean(TaosHandleProxyMatcher.class)
    public TaosHandleProxyMatcher taosHandleProxyMatcher(TaosSqlManager taosSqlManager) {
        return new TaosHandleProxyMatcher(taosSqlManager);
    }

}
