package com.ws.common;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * ClassName：CommonsCli
 * </p>
 * <p>
 * Description：${DESCRIPTION}
 * </p>
 * <p>
 * User：sun
 * </p>
 * <p>
 * CreateTime：2019-04-24 8:35
 * </p>
 * <p>
 * Modify：
 * </p>
 * <p>
 * ModifyTime：
 * </p>
 * <p>
 * Commont：
 * </p>
 * <p>
 * Copyright (c)
 * </p>
 *
 * @author sun
 * @version 1.0.0
 */
public class CommonsCli {

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("t", false, "first test");
        options.addOption("q", true, "second test");
        DefaultParser defaultParser = new DefaultParser();
        CommandLine cmd = defaultParser.parse(options, args);
        if (cmd.hasOption("t")) {
            System.out.println(cmd.getOptionValue("t"));
        }

        cmd.getArgList().forEach(System.out::println);


    }

    @org.junit.Test
    public void name() {

    }
}
