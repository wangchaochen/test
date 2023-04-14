package com.sense.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    @Primary
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceUrl); //数据源
        config.setUsername(user); //用户名
        config.setPassword(password); //密码
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(120000);
        config.setMaxLifetime(300000);
//        config.setMinimumIdle(10);
//        config.setMaximumPoolSize(100);
        config.setMinimumIdle(20);
        config.setMaximumPoolSize(50);

        HikariDataSource ds = new HikariDataSource(config);
        return ds;

    }
}
