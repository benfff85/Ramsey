package com.benajaminleephoto.ramsey.cliqueChecker;

public class CliqueCheckerFactory {

    private CliqueCheckerParallel cliqueCheckerParallel;


    public CliqueCheckerFactory() {
        cliqueCheckerParallel = new CliqueCheckerParallel();
    }


    public CliqueChecker getCliqueChecker() {
        return cliqueCheckerParallel;
    }

}
