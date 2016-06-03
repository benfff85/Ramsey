package com.benajaminleephoto.ramsey.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigFileReader {

    private InputStream inputStream;
    private static final Logger logger = LoggerFactory.getLogger(ConfigFileReader.class.getName());
    private Properties properties;


    public void initializeConfig() throws IOException {

        try {
            properties = new Properties();
            String propFileName = "config.properties";
            logger.info("Attempting to load configuration from {}", propFileName);

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
            }

            logger.debug("Configuration loaded to properties object");

            logger.debug("Setting CLIQUE_SIZE");
            Config.CLIQUE_SIZE = Integer.parseInt(properties.getProperty("cliqueSize"));
            logger.info("CLIQUE_SIZE: {}", Config.CLIQUE_SIZE);

            logger.debug("Setting NUM_OF_ELEMENTS");
            Config.NUM_OF_ELEMENTS = Integer.parseInt(properties.getProperty("numOfElements"));
            logger.info("NUM_OF_ELEMENTS: {}", Config.NUM_OF_ELEMENTS);

            logger.debug("Setting MUTATE_COUNT");
            Config.MUTATE_COUNT = Integer.parseInt(properties.getProperty("mutateCount"));
            logger.info("MUTATE_COUNT: {}", Config.MUTATE_COUNT);

            logger.debug("Setting MUTATE_INTERVAL");
            Config.MUTATE_INTERVAL = Integer.parseInt(properties.getProperty("mutateInterval"));
            logger.info("MUTATE_INTERVAL: {}", Config.MUTATE_INTERVAL);

            logger.debug("Setting MUTATE_COMPREHENSIVE_EDGE_RANGE");
            Config.MUTATE_COMPREHENSIVE_EDGE_RANGE = Integer.parseInt(properties.getProperty("mutateComprehensiveEdgeRange"));
            logger.info("MUTATE_COMPREHENSIVE_EDGE_RANGE: {}", Config.MUTATE_COMPREHENSIVE_EDGE_RANGE);

            logger.debug("Setting CLIQUE_SEARCH_THREAD_COUNT");
            Config.CLIQUE_SEARCH_THREAD_COUNT = Integer.parseInt(properties.getProperty("cliqueSearchThreadCount"));
            logger.info("CLIQUE_SEARCH_THREAD_COUNT: {}", Config.CLIQUE_SEARCH_THREAD_COUNT);

            logger.debug("Setting CHKPT_FILE_PATH");
            Config.CHKPT_FILE_PATH = properties.getProperty("checkpointFilePath");
            logger.info("CHKPT_FILE_PATH: {}", Config.CHKPT_FILE_PATH);

            logger.debug("Setting CHKPT_FILE_MASK");
            Config.CHKPT_FILE_MASK = properties.getProperty("checkpointFileMask");
            logger.info("CHKPT_FILE_MASK: {}", Config.CHKPT_FILE_MASK);

            logger.debug("Setting MAX_FILE_PATH");
            Config.MAX_FILE_PATH = properties.getProperty("maxFilePath");
            logger.info("MAX_FILE_PATH: {}", Config.MAX_FILE_PATH);

            logger.debug("Setting MAX_FILE_MASK");
            Config.MAX_FILE_MASK = properties.getProperty("maxFileMask");
            logger.info("MAX_FILE_MASK: {}", Config.MAX_FILE_MASK);

            logger.debug("Setting SOLUTION_FILE_PATH");
            Config.SOLUTION_FILE_PATH = properties.getProperty("solutionFilePath");
            logger.info("SOLUTION_FILE_PATH: {}", Config.SOLUTION_FILE_PATH);

            logger.debug("Setting SOLUTION_FILE_MASK");
            Config.SOLUTION_FILE_MASK = properties.getProperty("solutionFileMask");
            logger.info("SOLUTION_FILE_MASK: {}", Config.SOLUTION_FILE_MASK);

            logger.debug("Setting EMAIL_SOLUTION_IND");
            Config.EMAIL_SOLUTION_IND = Boolean.parseBoolean(properties.getProperty("emailSolutionInd"));
            logger.info("EMAIL_SOLUTION_IND: {}", Config.EMAIL_SOLUTION_IND);

            logger.debug("Setting EMAIL_ADDRESS");
            Config.EMAIL_ADDRESS = properties.getProperty("emailAddress");
            logger.info("EMAIL_ADDRESS: {}", Config.EMAIL_ADDRESS);

            logger.debug("Setting EMAIL_USER_ID");
            Config.EMAIL_USER_ID = properties.getProperty("emailUserID");
            logger.info("EMAIL_USER_ID: {}", Config.EMAIL_USER_ID);

            logger.debug("Setting EMAIL_PASSWORD");
            Config.EMAIL_PASSWORD = properties.getProperty("emailPassword");
            logger.info("EMAIL_PASSWORD: {}", Config.EMAIL_PASSWORD);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

}
