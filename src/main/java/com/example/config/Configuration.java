package com.example.config;

import com.example.config.MySQL.MySqlConfiguration;
import org.springframework.context.annotation.Import;

@org.springframework.context.annotation.Configuration
@Import({MySqlConfiguration.class})
public class Configuration {
}
