import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.nio.file.*;


class Parser {
	String commandName = "";
	String[] args;

	// This method will divide the input into commandName and args
	// where "input" is the string command entered by the user
	public boolean parse(String input) {
		try {
			input = input.trim();

			// Handle single commands without arguments
			if (input.equalsIgnoreCase("pwd") || input.equalsIgnoreCase("ls")) {
				commandName = input.toLowerCase();
				args = new String[0];
				return true;
			}

			// Handle cd without arguments (go to home)
			if (input.equalsIgnoreCase("cd")) {
				commandName = "cd";
				args = new String[0];
				return true;
			}

			// Handle commands with arguments
			String[] parts = input.split("\\s+", 2);
			if (parts.length >= 1) {
				String cmd = parts[0].toLowerCase();
				if (CommandList.COMMANDS.contains(cmd)) {
					commandName = cmd;
					if (parts.length == 2) {
						args = parts[1].trim().split("\\s+");
					} else {
						args = new String[0];
					}
					return true;
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		commandName = "";
		args = new String[0];
		return false;
	}

	public String getCommandName() {
		return commandName;
	}

	public String[] getArgs() {
		return args;
	}
}

class CommandList {
	public static List<String> COMMANDS = Arrays.asList("pwd", "cd", "ls", "mkdir", "rmdir",
		"touch", "cp", "cp -r", "rm", "cat", "wc", ">", ">>", "zip", "unzip");
}

class Terminal {
	// Implement The All commands in a methods
	private Parser parser;
	static File currentDirectory;

	public Terminal(Parser parser) {
		this.parser = parser;
		this.currentDirectory = new File(System.getProperty("user.dir"));
	}

	public static File getCurrentDirectory() {
		return currentDirectory;
	}
	public void pwd() {		
			System.out.println(currentDirectory.getAbsolutePath());		
	}

	public void cd(String[] args) {
		try {
			if (args == null || args.length == 0) {
				currentDirectory = new File(System.getProperty("user.dir"));
				return;
			}
			else if(args.length == 1 && args[0].equals("..")){
				currentDirectory = currentDirectory.getParentFile();
				return; 
			}
			else{
				if(new File( args[0]).isAbsolute() && new File(args[0]).isDirectory()){
					currentDirectory = new File(args[0]);
					return;
				}
				else{
					Path temCurrentDirectory = Paths.get(currentDirectory.getAbsolutePath(), args[0]);
					if(Files.isDirectory(temCurrentDirectory)){
						currentDirectory = temCurrentDirectory.toFile();
						currentDirectory = currentDirectory.getAbsoluteFile();
						return;
					}
					else{
						System.err.println("cd: " + args[0] + ": No such file or directory");
						return;
					}
				}
			}
		}catch (Exception e) {
			System.err.println("cd: " + e.getMessage());
		}
	}

	public void ls() {
		try {
			File currDir = currentDirectory;

			File[] files = currDir.listFiles();

			if (files == null) System.err.println("ls: permission denied");

			Arrays.sort(files);

			for (File file : files) {
				if (!file.getName().startsWith(".")) {
					if (file.isDirectory()) {
						System.out.println(file.getName() + "/");
					} else {
						System.out.println(file.getName());
					}
				}
			}
		}
		catch (SecurityException e) {
			System.err.println("ls: permission denied");
		} catch (Exception e) {
			System.err.println("ls: " + e.getMessage());
		}
	}

	public void mkdir(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("mkdir: missing operands");
			return;
		}

		// file/s creation
		for (int i = 0; i < args.length; i++) {
			String dirName = args[i];
			File dir = new File(dirName);

			if (dir.exists()) {
				System.out.println(dirName + " dir already exists");
			} else if (dir.mkdir()) {
				System.out.println(dirName + " dir created");
			} else {
				System.out.println("mkdir: cannot create directory '" + dirName +
					"': Permission denied or invalid path");
			}
		}
	}

	public void rmdir(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("rmdir: missing operand");
			return;
		}

		for (int i = 0; i < args.length; i++) {
			String dirName = args[i];

			// Handle rmdir * case - remove all empty directories
			if (dirName.equals("*")) {
				File currentDir = new File(".");
				File[] files = currentDir.listFiles();

				if (files != null) {
					for (File file : files) {
						if (file.isDirectory()) {
							if (file.list().length == 0) {
								if (file.delete()) {
									System.out.println(file.getName() + " directory removed");
								} else {
									System.out.println("rmdir: failed to remove '" +
										file.getName() + "': Permission denied");
								}
							}
						}
					}
				}
			} else {
				// Handle specific directory removal
				File dir = new File(dirName);

				if (!dir.exists()) {
					System.out.println(
						"failed to remove '" + dirName + "': No such file or directory");
				} else if (!dir.isDirectory()) {
					System.out.println("failed to remove '" + dirName + "': Not a directory");
				} else if (dir.list().length > 0) {
					System.out.println("failed to remove '" + dirName + "': Directory not empty");
				} else if (dir.delete()) {
					System.out.println(dirName + " directory removed");
				} else {
					System.out.println("failed to remove '" + dirName + "': Permission denied");
				}
			}
		}
	}

	public void cat(String[] args) {
		if (args == null || args.length == 0) {
			System.err.println("cat: call with 0 arguments");
		}

		// one argument
		if (args.length == 1) {
			try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			} catch (Exception e) {
				System.out.println("cat: " + args[0] + ": " + e.getMessage());
			}
		}
		// two arguments ( concatenate both files content )
		else {
			for (String fileName : args) {
				try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} catch (Exception e) {
					System.out.println("cat: " + fileName + ": " + e.getMessage());
				}
			}
		}
	}


	// word count
	public void wc(String fileName) {
		Integer lines = 0, words = 0, chars = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines++;
				chars += line.length();

				if (!line.trim().isEmpty()) {
					String[] wordArray = line.trim().split("\\s+");
					words += wordArray.length;
				}
			}
		} catch (java.io.FileNotFoundException e) {
			System.err.println("wc: " + fileName + " : No such file or directory");
		} catch (java.io.IOException e) {
			System.err.println("wc: " + fileName + ": " + e.getMessage());
		} catch (SecurityException e) {
			System.err.println("wc: " + fileName + ": permission denied");
		} catch (Exception e) {
			System.out.println("wc: " + fileName + ": " + e.getMessage());
		}
		System.out.println(lines + " " + words + " " + chars + " " + fileName);
	}


	// Helper method to add a single file to zip
	private boolean addFileToZip(ZipOutputStream zos, File file, String basePath) {
		try (FileInputStream fis = new FileInputStream(file)) {
			String entryName = basePath + file.getName();
			ZipEntry zipEntry = new ZipEntry(entryName);
			zos.putNextEntry(zipEntry);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}

			zos.closeEntry();
			System.out.println("Added: " + entryName);
			return true;

		} catch (IOException e) {
			System.err.println("zip: Error adding " + file.getName() + ": " + e.getMessage());
			return false;
		}
	}

	// Helper method to recursively add directory to zip
	private boolean addDirectoryToZip(ZipOutputStream zos, File directory, String basePath) {
		boolean hasFiles = false;
		File[] files = directory.listFiles();

		if (files == null) {
			System.err.println("zip: Cannot read directory " + directory.getName());
			return false;
		}

		String dirPath = basePath + directory.getName() + "/";

		// Add directory entry (empty directories)
		try {
			ZipEntry dirEntry = new ZipEntry(dirPath);
			zos.putNextEntry(dirEntry);
			zos.closeEntry();
		} catch (IOException e) {
			System.err.println("zip: Error adding directory " + directory.getName());
		}

		// Add all files and subdirectories
		for (File file : files) {
			if (file.isDirectory()) {
				// Recursively add subdirectory
				if (addDirectoryToZip(zos, file, dirPath)) {
					hasFiles = true;
				}
			} else {
				// Add file
				if (addFileToZip(zos, file, dirPath)) {
					hasFiles = true;
				}
			}
		}

		return hasFiles;
	}


	public void zip(String[] args) {
		if (args == null || args.length < 2) {
			System.err.println("zip: missing operands");
			System.err.println("Usage: zip [-r] <archive_name.zip> <file1> [file2] [file3] ...");
			return;
		}

		boolean recursive = false;
		int startIndex = 0;

		// Check for -r flag
		if (args[0].equals("-r")) {
			recursive = true;
			startIndex = 1;
			if (args.length < 3) {
				System.err.println("zip: missing operands");
				System.err.println("Usage: zip -r <archive_name.zip> <directory>");
				return;
			}
		}

		String zipFileName = args[startIndex];
		// Add .zip extension if not present
		if (!zipFileName.toLowerCase().endsWith(".zip")) {
			zipFileName += ".zip";
		}

		try (FileOutputStream fos = new FileOutputStream(zipFileName);
			 ZipOutputStream zos = new ZipOutputStream(fos)) {
			boolean hasValidFiles = false;

			// Process each file/directory to be zipped
			for (int i = startIndex + 1; i < args.length; i++) {
				String fileName = args[i];
				File file = new File(fileName);

				if (!file.exists()) {
					System.err.println("zip: " + fileName + ": No such file or directory");
					continue;
				}

				if (file.isDirectory()) {
					if (recursive) {
						// Recursively add directory
						if (addDirectoryToZip(zos, file, "")) {
							hasValidFiles = true;
						}
					} else {
						System.err.println("zip: " + fileName +
							": Is a directory (skipping, use -r for recursive)");
					}
				} else {
					// Add single file
					if (addFileToZip(zos, file, "")) {
						hasValidFiles = true;
					}
				}
			}

			if (hasValidFiles) {
				System.out.println("Archive '" + zipFileName + "' created successfully");
			} else {
				System.err.println("zip: No valid files to archive");
				// Delete empty zip file
				new File(zipFileName).delete();
			}

		} catch (IOException e) {
			System.err.println("zip: Error creating archive: " + e.getMessage());
		} catch (SecurityException e) {
			System.err.println("zip: Permission denied");
		} catch (Exception e) {
			System.err.println("zip: " + e.getMessage());
		}
	}


	public void unzip(String[] args) {
		if (args == null || args.length == 0) {
			System.err.println("unzip: missing operands");
			System.err.println("Usage: unzip <archive_name.zip> [-d destination_directory]");
			return;
		}

		String zipFileName = args[0];
		String extractPath = "."; // Default to current directory

		// Check for -d flag and destination directory
		if (args.length >= 3 && args[1].equals("-d")) {
			extractPath = args[2];
		} else if (args.length == 2) {
			System.err.println("unzip: option requires an argument -- d");
			System.err.println("Usage: unzip <archive_name.zip> [-d destination_directory]");
			return;
		}

		// Add .zip extension if not present
		if (!zipFileName.toLowerCase().endsWith(".zip")) {
			zipFileName += ".zip";
		}

		// Check if zip file exists
		File zipFile = new File(zipFileName);
		if (!zipFile.exists()) {
			System.err.println("unzip: " + zipFileName + ": No such file or directory");
			return;
		}

		if (!zipFile.isFile()) {
			System.err.println("unzip: " + zipFileName + ": Not a file");
			return;
		}

		// Create destination directory if it doesn't exist
		File destDir = new File(extractPath);
		if (!destDir.exists()) {
			if (!destDir.mkdirs()) {
				System.err.println("unzip: Cannot create destination directory: " + extractPath);
				return;
			}
			System.out.println("Created destination directory: " + extractPath);
		}

		if (!destDir.isDirectory()) {
			System.err.println("unzip: " + extractPath + ": Not a directory");
			return;
		}

		try (FileInputStream fis = new FileInputStream(zipFile);
			 ZipInputStream zis = new ZipInputStream(fis)) {
			ZipEntry entry;
			boolean hasExtracted = false;

			while ((entry = zis.getNextEntry()) != null) {
				String entryName = entry.getName();

				// Security check - skip entries that try to go outside destination directory
				if (entryName.contains("..")) {
					System.err.println("unzip: Skipping dangerous path: " + entryName);
					continue;
				}

				// Create full path in destination directory
				File outputFile = new File(destDir, entryName);

				if (entry.isDirectory()) {
					// Create directory
					if (outputFile.mkdirs()) {
						System.out.println("Created directory: " + outputFile.getPath());
					} else if (outputFile.exists()) {
						System.out.println("Directory already exists: " + outputFile.getPath());
					} else {
						System.err.println(
							"unzip: Failed to create directory: " + outputFile.getPath());
					}
				} else {
					// Create parent directories if they don't exist
					File parentDir = outputFile.getParentFile();
					if (parentDir != null && !parentDir.exists()) {
						parentDir.mkdirs();
					}

					// Extract file
					try (FileOutputStream fos = new FileOutputStream(outputFile)) {
						byte[] buffer = new byte[1024];
						int length;
						while ((length = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, length);
						}
						System.out.println("Extracted: " + outputFile.getPath());
						hasExtracted = true;
					} catch (IOException e) {
						System.err.println(
							"unzip: Error extracting " + entryName + ": " + e.getMessage());
					}
				}

				zis.closeEntry();
			}

			if (hasExtracted) {
				System.out.println("Archive '" + zipFileName + "' extracted successfully to '" +
					extractPath + "'");
			} else {
				System.out.println("unzip: No files extracted from " + zipFileName);
			}

		} catch (IOException e) {
			System.err.println("unzip: Error reading archive: " + e.getMessage());
		} catch (SecurityException e) {
			System.err.println("unzip: Permission denied");
		} catch (Exception e) {
			System.err.println("unzip: " + e.getMessage());
		}
	}


	public void touch() {}


	// This method will choose the suitable command method to be called
	public void chooseCommandAction() {
		if (parser != null) {
			switch (parser.getCommandName().toLowerCase()) {
				case "pwd":
					pwd();
					break;

				// Mohamed
				case "cd":
					cd(parser.getArgs());
					break;

				// AbuHamed
				case "ls":
					ls();
					break;

				// AbuHamed
				case "mkdir":
					mkdir(parser.getArgs());
					break;

				// AbuHamed
				case "rmdir":
					rmdir(parser.getArgs());
					break;

				// AbuHamed
				case "cat":
					cat(parser.getArgs());
					break;

				// AbuHamed
				case "wc":
					wc(parser.getArgs()[0]);
					break;

				// Zyad
				case "zip":
					zip(parser.getArgs());
					break;

				// Zyad
				case "unzip":
					unzip(parser.getArgs());
					break;

					// Joo
					// case "touch":
					// touch(parser.getArgs());
					// break;

					// Joo
					// case "cp":
					// cp(parser.getArgs());
					// break;

					// Joo
					// case "cp -r":
					// cp_r(parser.getArgs());
					// break;

					// Joo
					// case "rm":
					// rm(parser.getArgs());
					// break;

					// Zyad
					// case ">":
					// redirectOutput(parser.getArgs());
					// break;

					// Zyad
					// case ">>":
					// appendOutput(parser.getArgs());
					// break;


				default:
					System.out.println("Invalid command: " + parser.getCommandName());
					break;
			}
		}
	}
}

public class Main {
	static Parser parser = new Parser();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Terminal terminal = new Terminal(parser);

		while (true) {
			File cwd = terminal.getCurrentDirectory();
			System.out.println(cwd + ">");

			String input = scanner.nextLine();
			if (input.isEmpty()) {
				System.out.println("Empty Command!!");
				continue;
			}
			if (input.equalsIgnoreCase("EXIT")) {
				System.out.println("Thanks!!");
				scanner.close();
				return;
			}
			if (!parser.parse(input)) {
				System.out.println("wrong command or bad parameters!!");
				parser.commandName = "";
				parser.args = new String[0];
				continue;
			}

			terminal.chooseCommandAction();
		}
	}
}