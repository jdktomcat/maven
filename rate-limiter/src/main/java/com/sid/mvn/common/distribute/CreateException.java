package com.sid.mvn.common.distribute;

/**
 * 类概述：
 *
 * @author tangqi
 * @date 2021-11-10
 */
public class CreateException extends RuntimeException {

    public CreateException(String message) {
        super(message);
    }

    public CreateException(Throwable cause) {
        super(cause);
    }

    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }

}
