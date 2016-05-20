package com.benajaminleephoto.ramsey.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphFileReader {

    private static final Logger logger = LoggerFactory.getLogger(GraphFileReader.class.getName());


    public static String getLoaderStringFromFileInteractive() throws Exception {
        JFileChooser chooser = new JFileChooser();
        int retval;
        String output = "";

        // Select a file
        retval = chooser.showOpenDialog(null);

        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            output = getLoaderStringFromFile(file);
        } else {
            throw new Exception("File not selected");
        }
        return output;
    }


    public static String getLoaderStringFromFile(File file) throws Exception {
        String inputLine;
        StringTokenizer st;
        int nextInt;
        StringBuilder output = new StringBuilder();
        BufferedReader buffRead = null;

        try {

            buffRead = new BufferedReader(new FileReader(file));

            // Loop through file creating edges as we go
            for (int i = 0; i < Config.NUM_OF_ELEMENTS; i++) {
                inputLine = buffRead.readLine();
                if (inputLine == null) {
                    throw new Exception("Not enough lines in file");
                }

                st = new StringTokenizer(inputLine, ",");

                if (st.countTokens() != Config.NUM_OF_ELEMENTS) {
                    throw new Exception("Not enough elements in file row");
                }

                for (int j = 0; j < Config.NUM_OF_ELEMENTS; j++) {
                    nextInt = Integer.parseInt(st.nextToken());
                    if (j > i) {
                        output.append(nextInt);
                    }
                }
            }
            return output.toString();
        } catch (Exception e) {
            logger.error("Error occured while getting loader string from {}.", file.getName());
            throw e;
        } finally {
            buffRead.close();
        }

    }

}
