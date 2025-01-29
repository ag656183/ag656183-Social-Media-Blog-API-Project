package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        // User registration
        app.post("/register", this::registerUser);
        // User Login
        app.post("/login", this::loginUser);
        // Create Message
        app.post("/messages", this::createMessage);
        // Retrieve all messages
        app.get("/messages", this::getAllMessages);
        // Retrieve messages by ID
        app.get("/messages/{message_id}", this::getMessagesByID);
        // Delete message
        app.delete("messages/{message_id}", this::deleteMessage);
        // Update message
        app.patch("/messages/{message_id}", this::updateMessage);
        // Retrieve all message by particular user
        app.get("/accounts/{account_id/messages}", this::getMessageByUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}