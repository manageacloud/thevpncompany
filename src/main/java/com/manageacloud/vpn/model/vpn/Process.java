/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model.vpn;

public class Process {

    private String stdout;
    private String stderr;
    private int exitCode;

    public Process(String stdout, String stderr, int exitCode) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCode = exitCode;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public int getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return "Process{" +
                "stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                ", exitCode=" + exitCode +
                '}';
    }
}
