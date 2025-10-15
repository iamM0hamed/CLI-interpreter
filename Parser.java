public class Parser {
    String commandName = "";
    String[] args;
    
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){
        try 
        {
            input = input.trim();
            for(int i = 0 ; i < input.length() ; i++)
            {
                // note == compare references => to compare values use .equals
                if(input.equalsIgnoreCase("pwd")  || input.equalsIgnoreCase("ls") || input.equalsIgnoreCase("cd"))
                {
                    commandName = input;
                    args = new String[0];
                    return true;
                }
                // check if the current space is bad command or saperate the command and the args
                if(input.charAt(i) == ' ' && commandName.length() != 0)
                {
                    // bad command 
                    if(!CommandList.COMMANDS.contains(commandName.toLowerCase()))
                    {
                        throw new Exception("wrong command or bad parameters!!");
                    }
                    // the space is a separator between the command and the args
                    else
                    {
                        StringBuilder arguments = new StringBuilder(); 
                        // set the args
                        for (int j = i+1 ; j < input.length(); j++) {
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
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        commandName = "";
        args = new String[0];
        return false;
    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
}
