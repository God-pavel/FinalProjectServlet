package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.ProductService;
import com.pasha.trainingcourse.model.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static CommandManager commandManager;
    private final Map<String, Command> commandMap = new HashMap<>();


    private CommandManager() {
        commandMap.put("/users", new GetAllUsersCommand(new UserService()));
        commandMap.put("/login", new LoginCommand(new UserService()));
        commandMap.put("/registration", new RegistrationCommand(new UserService()));
        commandMap.put("/logout", new LogoutCommand());
        commandMap.put("/userEdit", new UserEditCommand(new UserService()));
        commandMap.put("/index", new IndexCommand());
        commandMap.put("/storage", new StorageCommand(new ProductService()));
        commandMap.put("/merchandise", new AddProductCommand(new ProductService()));
        commandMap.put("/productEdit", new ProductEditCommand(new ProductService()));
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