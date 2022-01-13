package com.louzx.swipe.core.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * @author louzx
 * @date 2022/1/13
 */
public class WxDataSource {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.wx")
    public DataSource dataSourceWx() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate jdbcTemplateWx (@Qualifier("dataSourceWx") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplateWx (@Qualifier("dataSourceWx") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
