package com.aex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static final String name = "Android Extrapktor";
	private static String command_pullfile = " pull data/app/";

	public static void main(String[] args) throws IOException, InterruptedException {
		String finalPath = "";
		String adb = "";
		Process p = Runtime.getRuntime().exec("figlet " + name);
		p.waitFor();
		System.out.println(readProcessOutput(p));
		System.out.println("");
		System.out.println("By M4v3r1cX");
		System.out.println("============");
		System.out.println("");
		try {
			adb = args[0];
			finalPath = args[1];
		} catch (Exception e) {
			finishProcessShamelessly(
					"Lacking arguments. Please include the path to adb and the path where the APKs will be stored/taken from.");
		}
		System.out.println("Choose your destiny:");
		System.out.println("====================");
		System.out.println("");
		System.out.println("[1] - Back up the APKs from your phone");
		System.out.println("[2] - Install APKs back into the phone from directory");
		System.out.println("");
		System.out.println("Write the number of your option, then press [Enter]");
		String input = System.console().readLine();
		if (input != null && !input.isEmpty()) {
			if (input.equals("1")) {
				backUpApks(finalPath, adb);
			} else if (input.equals("2")) {
				System.out.println("Option not yet implemented!");
			} else {
				finishProcessShamelessly("You think I'm fucking joking m8? Choose an option next time.");
			}
		} else {
			finishProcessShamelessly("You think I'm fucking joking m8? Choose an option next time.");
		}
	}

	private static void backUpApks(String finalPath, String adb) throws IOException, InterruptedException {
		System.out.println("I'm trying to connect to the phone...");
		System.out.println("");
		Process p = Runtime.getRuntime().exec(adb + " shell su -c 'ls data/app'");
		p.waitFor();
		String error = readProcessError(p);
		if (error != null && !error.isEmpty()) {
			finishProcessShamelessly(error + "It wasn't my fault, adb fucked up :(");
		}
		System.out.println("The APK Directory List was succesfully created! ^^");
		List<String> folders = storeProcessOutput(p);
		if (folders == null || folders.isEmpty()) {
			finishProcessShamelessly("But the file list is empty :( sorry!");
		}
		System.out.println("Ok, I'm trying to get every god damn apk in the list. This will take a while.");
		System.out.println("There's in total " + folders.size() + " APK files to transfer!");
		System.out.println("\nType [Y] to start, [N] to cancel process, and press enter.");
		String input = System.console().readLine();
		if (input.length() == 1) {
			if (input.equalsIgnoreCase("y")) {
				for (int i = 0; i < folders.size(); i++) {
					String s = folders.get(i);
					String adbcommand = adb + command_pullfile + s + "/base.apk " + finalPath;
					p = Runtime.getRuntime().exec(adbcommand);
					InputStreamReader is = new InputStreamReader(p.getInputStream());
					BufferedReader reader = new BufferedReader(is);
					String line = "";
					while ((line = reader.readLine()) != null) {
						System.out.println(line + " - processing (" + (i + 1) + "/"
								+ folders.size() + ")");
					}
					p.waitFor();
					error = readProcessError(p);
					if (error != null && !error.isEmpty()) {
						finishProcessShamelessly("I fucked up.\n" + error);
					} else {
						String[] parts = s.split("-");
						String name = parts[0];
						File file1 = new File(finalPath, "base.apk");
						File file2 = new File(finalPath, name + ".apk");
						boolean success = file1.renameTo(file2);
						if (success) {
							System.out.println("Succesfully pulled " + name + " from phone. (" + (i + 1) + "/"
									+ folders.size() + ")");
						} else {
							finishProcessShamelessly("Error trying to pull " + name + " from phone.");
						}
					}
				}
				System.out.println("Wow, that was like " + folders.size() + " files. All of them pulled off!");
				finishProcessShamelessly("No error at all!");
			} else {
				finishProcessShamelessly("Canceling process...");
			}
		} else {
			finishProcessShamelessly("You think I'm fucking joking m8? Choose an option next time.");
		}
	}

	private static void finishProcessShamelessly(String error) {
		System.out.println(error);
		System.out.println(name + " finished! Hope I could help you! ^^");
		System.exit(1);
	}

	private static String readProcessOutput(Process p) throws IOException {
		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line = "";
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}

		return output.toString();
	}

	private static List<String> storeProcessOutput(Process p) throws IOException {
		List<String> strings = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line = "";
		while ((line = reader.readLine()) != null) {
			strings.add(line);
		}

		return strings;
	}

	private static String readProcessError(Process p) throws IOException {
		StringBuilder output = new StringBuilder();
		BufferedReader reader;
		if (p.getErrorStream() != null && !p.getErrorStream().toString().isEmpty()) {
			reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		}

		return output.toString();
	}
}
