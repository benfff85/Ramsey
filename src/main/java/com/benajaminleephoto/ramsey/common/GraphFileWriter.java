package com.benajaminleephoto.ramsey.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphFileWriter {

    private static final Logger logger = LoggerFactory.getLogger(GraphFileWriter.class.getName());


    public static void writeSolutionFile() {
        writeGraphFile(Config.SOLUTION_FILE_PATH + Config.SOLUTION_FILE_MASK + RamseyLogger.getDateTimeStamp() + ".sol");
    }


    public static void writeCheckpointFile() {
        writeGraphFile(Config.CHKPT_FILE_PATH + Config.CHKPT_FILE_MASK + RamseyLogger.getDateTimeStamp() + ".chk");
    }


    public static void writeMaxFile() {
        writeGraphFile(Config.MAX_FILE_PATH + Config.MAX_FILE_MASK + "MAX" + ".chk");
    }


    /**
     * This is used to write a basic representation of an input CayleyGraph to a given file which
     * can later be loaded into this program.
     * 
     * @param cayleyGraph The CayleyGraph to be written to a file.
     * @param qualifiedFileName The file path and name where the CayleyGraph representation is to be
     *        written.
     */
    private static void writeGraphFile(String qualifiedFileName) {
        String content = ApplicationContext.getCayleyGraph().printCayleyGraphBasic();

        // Write the "content" string to file
        try {
            logger.info("Writing file: " + qualifiedFileName);
            File file = new File(qualifiedFileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(content);
            logger.error("Error while writing file: " + qualifiedFileName);
        }
    }

}
