package com.packages.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
 * @author Amarendra
 * @version 1.0
 * 
 */
public class WindowsCommandUtil {
	private static final Logger logger = Logger.getLogger(WindowsCommandUtil.class);

	/**
	 * Connects to IPC share of remote machine. Should be called before
	 * executing any command on remote machine
	 * 
	 * @param machine remote machine name or IP
	 * @param username user on remote machine
	 * @param password password for user on remote machine
	 */
	public static void connectForIPCShare(String machine, String username, String password) {
		try {
			String executeCmd = "cmd /c net use \\\\" + machine + "\\IPC$ /U:" + username + " " + password;
			logger.debug("DEV DEBUG: " + executeCmd);
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command(executeCmd);
			Process runtimeProcess = processBuilder.start();
			int cmdStatus = runtimeProcess.waitFor();
			System.out.println(cmdStatus);
		} catch (IOException | InterruptedException ex) {
			logger.debug("Error to connect to windows machine: ", ex);
		}
	}

	/**
	 * Disconnects from IPC share of remote machine. Should be called after
	 * commands has been executed
	 * 
	 * @param serverName
	 */
	public static void disconnectIPCShare(String serverName) {
		try {
			String executeCmd = "cmd /c net use /delete \\\\" + serverName + "\\IPC$";
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command(executeCmd);
			Process runtimeProcess = processBuilder.start();
			int cmdStatus = runtimeProcess.waitFor();
			System.out.println(cmdStatus);
		} catch (IOException | InterruptedException ex) {
			logger.debug("Error to disconect connection to windows machine: ", ex);
		}
	}

	/**
	 * This method is responsible to run locally command on windows machine
	 * 
	 * @param command
	 * @return command execution result
	 */
	public static String anyCMDCommandLocally(String command) {
		String line;
		String result = "";
		String executeCmd = "cmd /c" + " " + command;
		int processComplete = 0;
		Process runtimeProcess;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command(executeCmd);
			runtimeProcess = processBuilder.start();
			TimeUnit.SECONDS.sleep(60);
			processComplete = runtimeProcess.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(runtimeProcess.getInputStream()));
			line = reader.readLine();
			while (line != null) {
				if (result.compareTo("") == 0)
					result = line;
				else
					result = result + "\n" + line;
				line = reader.readLine();
			}
		} catch (IOException | InterruptedException e) {
			logger.debug("Error to execute command on windows: ", e);
		}
		if (processComplete == 0 || processComplete != 1056 || processComplete != 3240) {
			logger.fatal("It was not possible to execute command on Windows or exit code is unexpected: "
					+ processComplete);
		}
		return result;
	}
}
