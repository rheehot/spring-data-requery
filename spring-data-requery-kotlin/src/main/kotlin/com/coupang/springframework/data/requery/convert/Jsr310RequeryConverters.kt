package com.coupang.springframework.data.requery.convert

/**
 * JPA 2.1 converters to turn JSR-310 types into legacy {@link Date}s. To activate these converters make sure your
 * persistence provider detects them by including this class in the list of mapped classes. In Spring environments, you
 * can simply register the package of this class (i.e. {@code org.springframework.data.jpa.convert.threeten}) as package
 * to be scanned on e.g. the {@link LocalContainerEntityManagerFactoryBean}.
 */
object Jsr310RequeryConverters {


}