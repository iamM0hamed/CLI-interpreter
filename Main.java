import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

	public Terminal(Parser parser) {
		this.parser = parser;
	}

	public void pwd() {
		try {
			String currentPath = System.getProperty("user.dir");

			// Split the path by file separator
			String[] pathParts =
				currentPath.split(File.separator.equals("\\") ? "\\\\" : File.separator);

			if (pathParts.length >= 2) {
				// Print last two directories: outer and current
				String outerDir = pathParts[pathParts.length - 2];
				String currentDir = pathParts[pathParts.length - 1];
				System.out.println(outerDir + File.separator + currentDir);
			} else if (pathParts.length == 1) {
				// Only one directory (root case)
				System.out.println(pathParts[0]);
			} else {
				// Fallback: print full path
				System.out.println(currentPath);
			}

		} catch (SecurityException e) {
			System.err.println("pwd: Permission denied");
		} catch (Exception e) {
			System.err.println("pwd: " + e.getMessage());
		}
	}

	public void cd(String[] args) {
		// just for testing
		System.out.println("cd has been done");
		if (args == null || args.length == 0) {
			System.out.println("There is no arguments");
			return;
		}
		System.out.print("The arguments are: ");
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
	}

	public void ls() {
		try {
			File currDir = new File(".");

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

					// Zyad
					// case "zip":
					// zip(parser.getArgs());
					// break;

					// Zyad
					// case "unzip":
					// unzip(parser.getArgs());
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
			String cwd = System.getProperty("user.dir");
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