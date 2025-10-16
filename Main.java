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
			for (int i = 0; i < input.length(); i++) {
				// note == compare references => to compare values use .equals
				if (input.equalsIgnoreCase("pwd") || input.equalsIgnoreCase("ls") ||
					input.equalsIgnoreCase("cd")) {
					commandName = input;
					args = new String[0];
					return true;
				}
				// check if the current space is bad command or saperate the command and
				// the args
				if (input.charAt(i) == ' ' && commandName.length() != 0) {
					// bad command
					if (!CommandList.COMMANDS.contains(commandName.toLowerCase())) {
						throw new Exception("wrong command or bad parameters!!");
					}
					// the space is a separator between the command and the args
					else {
						StringBuilder arguments = new StringBuilder();
						// set the args
						for (int j = i + 1; j < input.length(); j++) {
							arguments.append(input.charAt(j));
						}
						String[] parts = arguments.toString().trim().split("\\s+");
						args = parts;
						return true;
					}
				}
				// commandName.append(input.charAt(i));
				commandName += input.charAt(i);
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
	static Parser parser = new Parser();
	//-------------------Implement The All commands in a methods, for
	// example:-------------------
	public String pwd() {
		// just for testing
		return "pwd has been done";
	}
	public void cd(String[] args) {
		// just for testing
		System.out.println("cd has been done");
		if (args.equals(null)) {
			System.out.println("There is no arguments");
			return;
		}
		System.out.println("The arguments are: ");
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
	}
	// ...
	// This method will choose the suitable command method to be called
	public void chooseCommandAction() {
		if (!parser.equals(null)) {
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
					// case "mkdir":
					//     mkdir(parser.getArgs());
					//     break;

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

	public static void Main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		var terminal = new Terminal();
		while (true) {
			String cwd = System.getProperty("user.dir");
			System.out.println(cwd + ">");

			// System.out.println("Please , enter a command...");
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
			// you have the command name && the arguments
			// implement your commands based on the partitions file
		}
	}
}
