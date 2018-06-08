package com.coupang.springframework.data.requery.repository.query;

import lombok.Data;

import java.io.Serializable;

/**
 * RequeryQueryOptions
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
@Data
public class RequeryQueryOptions implements Serializable {

    private static final long serialVersionUID = 3862046471696799725L;

    private Boolean cache;
    private Boolean count;
    private Boolean fullCount;

}
