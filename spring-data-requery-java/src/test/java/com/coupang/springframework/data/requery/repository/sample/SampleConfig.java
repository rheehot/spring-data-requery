package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRequeryRepositories
public class SampleConfig extends RequeryTestConfiguration {
}
