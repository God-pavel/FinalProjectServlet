package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static CommandManager commandManager;
    private final Map<String, Command> commandMap = new HashMap<>();
    private UserService userService = new UserService();

    private CommandManager() {
        commandMap.put("/users", new GetAllUsersCommand(userService));
        commandMap.put("/login", new LoginCommand(userService));
        commandMap.put("/registration", new RegistrationCommand(userService));
        commandMap.put("/logout", new LogoutCommand());
        commandMap.put("/userEdit", new UserEditCommand(userService));
        commandMap.put("/index", new IndexCommand());
    }

    public static CommandManager getInstance() {
        if (commandManager == null) {
            synchronized (CommandManager.class) {
                if (commandManager == null) {
                    commandManager = new CommandManager();
                }
            }
        }
        return commandManager;
    }


    public Command getCommand(String commandName) {
        for(String key : commandMap.keySet()){
            if(commandName.contains(key)) return commandMap.get(key);
        }

        return commandMap.get("/index");
    }
}