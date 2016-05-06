package com.benajaminleephoto.ramsey.cliqueChecker;

import com.benajaminleephoto.ramsey.common.CayleyGraph;

public class CliqueCheckerFactory {

    private CliqueCheckerParallel cliqueCheckerParallel;


    public CliqueCheckerFactory(CayleyGraph cayleyGraph) {
        cliqueCheckerParallel = new CliqueCheckerParallel(cayleyGraph);
    }


    public CliqueChecker getCliqueChecker() {
        return cliqueCheckerParallel;
    }

}
