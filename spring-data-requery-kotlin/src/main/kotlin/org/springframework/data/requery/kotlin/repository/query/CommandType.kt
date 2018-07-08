package org.springframework.data.requery.repository.query

import mu.KotlinLogging

/**
 * org.springframework.data.requery.repository.query.CommandType
 *
 * @author debop
 */
enum class CommandType {

    NONE,
    SELECT,
    INSERT,
    UPDATE,
    UPSERT,
    DELETE,
    REFRESH;

    companion object {
        private val log = KotlinLogging.logger { }

        fun parse(command: String?): CommandType {
            if(command.isNullOrBlank())
                return NONE

            if(command!! == "remove") {
                return DELETE
            }
            return CommandType.values()
                       .find { it.name == command.toUpperCase() }
                   ?: SELECT
        }
    }
}