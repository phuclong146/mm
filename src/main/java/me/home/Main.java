package me.home;

import me.home.job.AutoPayScheduler;
import me.home.view.ControlShell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        AutoPayScheduler.run();
        ControlShell.help();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String cmd;
        while (true) {
            cmd = reader.readLine();
            ControlShell.process(cmd);
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Bye Bye!")));
    }
}