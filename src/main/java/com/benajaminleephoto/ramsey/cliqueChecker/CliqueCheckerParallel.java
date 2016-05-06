package com.benajaminleephoto.ramsey.cliqueChecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.ApplicationContext;

public class CliqueCheckerParallel implements CliqueChecker {

    private CliqueCheckerThreadPool cliqueCheckerThreadPool;
    private static final Logger logger = LoggerFactory.getLogger(CliqueCheckerParallel.class.getName());


    public CliqueCheckerParallel() {
        logger.info("Initializing CliqueCheckerParallel");
        cliqueCheckerThreadPool = new CliqueCheckerThreadPool();
        logger.info("CliqueCheckerParallel successfully initialized");
    }


    public void findClique(String color) throws Exception {
        logger.info("Starting graph search for {} cliques", color);
        cliqueCheckerThreadPool.setThreadColor(color);
        cliqueCheckerThreadPool.runAllThreads();
        logger.info("Graph search for {} cliques complete", color);
        logger.debug("{} clique count : {}", color, ApplicationContext.getCayleyGraph().getCliqueCollection().getCliqueCount(color));
    }

}
