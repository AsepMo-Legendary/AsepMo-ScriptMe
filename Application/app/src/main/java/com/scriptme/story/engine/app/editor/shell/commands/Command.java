package com.scriptme.story.engine.app.editor.shell.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import com.scriptme.story.engine.app.editor.shell.RootCommands;
import com.scriptme.story.engine.app.editor.shell.Shell;
import com.scriptme.story.engine.app.editor.shell.util.Log;
import com.scriptme.story.engine.app.editor.shell.util.BrokenBusyboxException;

public abstract class Command {
    final String command[];
    boolean finished = false;
    boolean brokenBusyboxDetected = false;
    int exitCode;
    int id;
    int timeout = RootCommands.DEFAULT_TIMEOUT;
    Shell shell = null;

    public Command(String... command) {
        this.command = command;
    }

    public Command(int timeout, String... command) {
        this.command = command;
        this.timeout = timeout;
    }

    /**
     * This is called from Shell after adding it
     * 
     * @param shell
     * @param id
     */
    public void addedToShell(Shell shell, int id) {
        this.shell = shell;
        this.id = id;
    }

    /**
     * Gets command string executed on the shell
     * 
     * @return
     */
    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.length; i++) {
            // redirect stderr to stdout
            sb.append(command[i] + " 2>&1");
            sb.append('\n');
        }
        Log.d(RootCommands.TAG, "Sending command(s): " + sb.toString());
        return sb.toString();
    }

    public void writeCommand(OutputStream out) throws IOException {
        out.write(getCommand().getBytes());
    }

    public void processOutput(String line) {
        Log.d(RootCommands.TAG, "ID: " + id + ", Output: " + line);

        /*
         * Try to detect broken toolbox/busybox binaries (see
         * https://code.google.com/p/busybox-android/issues/detail?id=1)
         * 
         * It is giving "Value too large for defined data type" on certain file operations (e.g. ls
         * and chown) in certain directories (e.g. /data/data)
         */
        if (line.contains("Value too large for defined data type")) {
            Log.e(RootCommands.TAG, "Busybox is broken with high probability due to line: " + line);
            brokenBusyboxDetected = true;
        }

        // now execute specific output parsing
        output(id, line);
    }

    public abstract void output(int id, String line);

    public void processAfterExecution(int exitCode) {
        Log.d(RootCommands.TAG, "ID: " + id + ", ExitCode: " + exitCode);

        afterExecution(id, exitCode);
    }

    public abstract void afterExecution(int id, int exitCode);

    public void commandFinished(int id) {
        Log.d(RootCommands.TAG, "Command " + id + " finished.");
    }

    public void setExitCode(int code) {
        synchronized (this) {
            exitCode = code;
            finished = true;
            commandFinished(id);
            this.notifyAll();
        }
    }

    /**
     * Close the shell
     * 
     * @param reason
     */
    public void terminate(String reason) {
        try {
            shell.close();
            Log.d(RootCommands.TAG, "Terminating the shell.");
            terminated(reason);
        } catch (IOException e) {
        }
    }

    public void terminated(String reason) {
        setExitCode(-1);
        Log.d(RootCommands.TAG, "Command " + id + " did not finish, because of " + reason);
    }

    /**
     * Waits for this command to finish and forwards exitCode into afterExecution method
     * 
     * @param timeout
     * @throws java.util.concurrent.TimeoutException
     * @throws BrokenBusyboxException
     */
    public void waitForFinish() throws TimeoutException, BrokenBusyboxException {
        synchronized (this) {
            while (!finished) {
                try {
                    this.wait(timeout);
                } catch (InterruptedException e) {
                    Log.e(RootCommands.TAG, "InterruptedException in waitForFinish()", e);
                }

                if (!finished) {
                    finished = true;
                    terminate("Timeout");
                    throw new TimeoutException("Timeout has occurred.");
                }
            }

            if (brokenBusyboxDetected) {
                throw new BrokenBusyboxException();
            }

            processAfterExecution(exitCode);
        }
    }

}
