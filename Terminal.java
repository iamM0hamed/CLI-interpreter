import java.util.Scanner;

public class Terminal {
    static Parser parser = new Parser();
    //-------------------Implement The All commands in a methods, for example:-------------------
    public String pwd(){
        // just for testing
        return "pwd has been done";
    }
    public void cd(String[] args){
        // just for testing
        System.out.println("cd has been done");
        if (args.equals(null)) 
        {
            System.out.println("There is no arguments");
            return;
        }
        System.out.println("The arguments are: ");
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }
    // ...
    //This method will choose the suitable command method to be called
    public void chooseCommandAction(){
        if(!parser.equals(null))
        {
            switch (parser.getCommandName().toLowerCase()) {
                case "pwd":
                    System.out.println(pwd()); 
                    break;

                case "cd":
                    cd(parser.getArgs());
                    break;

                // case "ls":
                //     ls();
                //     break;

                // case "mkdir":
                //     mkdir(parser.getArgs());
                //     break;

                // case "rmdir":
                //     rmdir(parser.getArgs());
                //     break;

                // case "touch":
                //     touch(parser.getArgs());
                //     break;

                // case "cp":
                //     cp(parser.getArgs());
                //     break;

                // case "cp -r":
                //     cp_r(parser.getArgs());
                //     break;

                // case "rm":
                //     rm(parser.getArgs());
                //     break;

                // case "cat":
                //     cat(parser.getArgs());
                //     break;

                // case "wc":
                //     wc(parser.getArgs());
                //     break;

                // case ">":
                //     redirectOutput(parser.getArgs());
                //     break;

                // case ">>":
                //     appendOutput(parser.getArgs());
                //     break;

                // case "zip":
                //     zip(parser.getArgs());
                //     break;

                // case "unzip":
                //     unzip(parser.getArgs());
                //     break;

                default:
                    System.out.println("Invalid command: " + parser.getCommandName());
                    break;
            }

        }
    }
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in); 
        var terminal = new Terminal();
        while(true)
        {
            String cwd = System.getProperty("user.dir");
            System.out.println(cwd + ">");

            // System.out.println("Please , enter a command...");
            String input = scanner.nextLine();
            if(input.isEmpty())
            {
                System.out.println("Empty Command!!");
                continue;
            }
            if (input.equalsIgnoreCase("EXIT")) {
                System.out.println("Thanks!!");
                scanner.close();
                return;
            }
            if(!parser.parse(input))
            {
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
