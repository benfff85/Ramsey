package com.benajaminleephoto.ramsey.cliqueChecker;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.ApplicationContext;
import com.benajaminleephoto.ramsey.common.Config;

public class CliqueCheckerThreadPool {

    private Set<Callable<Integer>> threads;
    private ExecutorService executor;
    private static final Logger logger = LoggerFactory.getLogger(CliqueCheckerThreadPool.class.getName());


    public CliqueCheckerThreadPool() {
        logger.info("Creating new clique checker thread pool");
        threads = new HashSet<Callable<Integer>>(Config.CLIQUE_SEARCH_THREAD_COUNT);
        initializeThreads();
        initializeExecutor();
        CliqueCheckerThread.cayleyGraph = ApplicationContext.getCayleyGraph();
        logger.info("Clique checker thread pool created successfully");
    }


    private void initializeThreads() {
        logger.info("Initializing threads");
        for (int i = 0; i < Config.CLIQUE_SEARCH_THREAD_COUNT; i++) {
            threads.add(new CliqueCheckerThread(i));
            logger.info("Thread {} initialized successfully", i);
        }
        logger.info("All threads initialized successfully");
    }


    private void initializeExecutor() {
        logger.info("Initializing executor");
        executor = Executors.newFixedThreadPool(Config.CLIQUE_SEARCH_THREAD_COUNT);
        logger.info("Executor initialzied successfully");
    }


    public void runAllThreads() throws Exception {
        executor.invokeAll(threads);
        VertexQueue.resetVertexId();
    }


    public static void setThreadColor(String color) {
        CliqueCheckerThread.color = color;
    }

}
