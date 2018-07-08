package org.springframework.data.requery.repository.sample

import org.springframework.context.annotation.Configuration
import org.springframework.data.requery.kotlin.configs.RequeryTestConfiguration
import org.springframework.data.requery.kotlin.repository.config.EnableRequeryRepositories

/**
 * org.springframework.data.requery.repository.sample.SampleConfig
 *
 * @author debop
 */
@Configuration
@EnableRequeryRepositories
class SampleConfig: RequeryTestConfiguration()