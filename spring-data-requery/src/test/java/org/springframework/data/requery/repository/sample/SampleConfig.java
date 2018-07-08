package org.springframework.data.requery.repository.sample;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.configs.RequeryTestConfiguration;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;

@Configuration
@EnableRequeryRepositories
public class SampleConfig extends RequeryTestConfiguration {
}
