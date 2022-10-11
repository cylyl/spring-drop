package com.github.cylyl.springdrop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOG {
    private static Logger logger;

    public static void info(String s) {
        getLogger().info(s);
    }

    private static Logger getLogger() {
        if(logger == null) {
            logger = LoggerFactory.getLogger(LOG.class);
        }
        return logger;
    }
}
