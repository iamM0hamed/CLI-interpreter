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
	public static List<String> COMMANDS = Arrays.asList("pwd", "cd", "ls", "mkdir",
		"rmdir", "touch", "cp", "cp -r", "rm", "cat", "wc", ">", ">>", "zip", "unzip");
}

class Terminal {
	// Implement The All commands in a methods
	private Parser parser;

	public Terminal(Parser parser) {
		this.parser = parser;
	}

	public String pwd() {
		// just for testing
		return "pwd has been done";
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

	public void mkdir(String[] args) {
		System.out.println("this is mkdir");
	}

	// This method will choose the suitable command method to be called
	public void chooseCommandAction() {
		if (parser != null) {
			switch (parser.getCommandName().toLowerCase()) {
				case "pwd":
					System.out.println(pwd());
					break;

				case "cd":
					cd(parser.getArgs());
					break;

				// Mohamed
				// case "ls":
				//     ls();
				//     break;

				// AbuHamed
				case "mkdir":
					mkdir(parser.getArgs());
					break;

					// AbuHamed
					// case "rmdir":
					//     rmdir(parser.getArgs());
					//     break;

					// Joo
					// case "touch":
					//     touch(parser.getArgs());
					//     break;

					// Joo
					// case "cp":
					//     cp(parser.getArgs());
					//     break;

					// Joo
					// case "cp -r":
					//     cp_r(parser.getArgs());
					//     break;

					// Joo
					// case "rm":
					//     rm(parser.getArgs());
					//     break;

					// AbuHamed
					// case "cat":
					//     cat(parser.getArgs());
					//     break;

					// Zyad
					// case "wc":
					//     wc(parser.getArgs());
					//     break;

					// Zyad
					// case ">":
					//     redirectOutput(parser.getArgs());
					//     break;

					// Zyad
					// case ">>":
					//     appendOutput(parser.getArgs());
					//     break;

					// Zyad
					// case "zip":
					//     zip(parser.getArgs());
					//     break;

					// Zyad
					// case "unzip":
					//     unzip(parser.getArgs());
					//     break;

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