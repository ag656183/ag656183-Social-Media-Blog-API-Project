package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

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
        app.post("/register", this::registerUserHandler);
        // User Login
        app.post("/login", this::loginUserHandler);
        // Create Message
        app.post("/messages", this::createMessageHandler);
        // Retrieve all messages
        app.get("/messages", this::getAllMessagesHandler);
        // Retrieve messages by ID
        app.get("/messages/{message_id}", this::getMessagesByIdHandler);
        // Delete message
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        // Update message
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        // Retrieve all message by particular user
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


    // Register User
    private void registerUserHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account createdAccount = accountService.registerUser(account.getUsername(), account.getPassword());

        if (createdAccount != null) {
            ctx.json(createdAccount);
        } else {
            ctx.status(400);
        }
    }


    // Login user
    private void loginUserHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.loginUser(account.getUsername(), account.getPassword());
    
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401); // Unauthorized
        }
    }


    // Create message
    private void createMessageHandler(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400);
        }
    }


    // Get all messages
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages); // Always return 200 OK with the message list (even if empty)
    }


    // Get all message by message id
    private void getMessagesByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
    
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200); // Return 200 with an empty response if message doesn't exist
        }
    }


    // Delete message handler
    private void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage != null) {
            ctx.json(deletedMessage); // Return deleted message JSON
        } else {
            ctx.status(200); // Return 200 with an empty response if message didn't exist
        }
    }


    // Update message handler
    private void updateMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message requestBody = ctx.bodyAsClass(Message.class);

        Message updatedMessage = messageService.updateMessage(messageId, requestBody.getMessage_text());

        if (updatedMessage != null) {
            ctx.json(updatedMessage); // Return updated message JSON
        } else {
            ctx.status(400); // Return 400 if update failed
        }
    }


    // Get messages handler
    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountId);
        ctx.json(messages); // Always return 200 OK with message list (even if empty)
    }
}