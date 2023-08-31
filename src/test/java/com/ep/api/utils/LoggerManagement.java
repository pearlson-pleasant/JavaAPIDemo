package com.ep.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManagement {

	private static final Logger logger = LogManager.getLogger();

	public static Logger getLogger() {
        return logger;
    }
}
