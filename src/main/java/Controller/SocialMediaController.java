package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;

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
        app.post("/register", this::registerUserHandler);
        // User Login
        app.post("/login", this::loginUserHandler);
        // Create Message
        app.post("/messages", this::createMessageHandler);
        // Retrieve all messages
        app.get("/messages", this::getAllMessagesHandler);
        // Retrieve messages by ID
        app.get("/messages/{message_id}", this::getMessagesByIDHandler);
        // Delete message
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        // Update message
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        // Retrieve all message by particular user
        app.get("/accounts/{account_id/messages}", this::getMessagesByUserHandler);

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
        Optional<Account> createdAccount = accountService.register(account);

        if(createdAccount.isPresent()) {
            ctx.status(200).json(createdAccount);
        }
        else {
            ctx.status(400);
        }
    }


    // Login user
    private void loginUserHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Optional<Account> authenticatedUser = accountService.login(account.getUsername(), account.getPassword());

        if(authenticatedUser.isPresent()) {
            ctx.status(200).json(authenticatedUser.get());
        }
        else {
            ctx.status(401);
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
        ctx.json(messageService.getAllMessages());
    }


    // Get messages by ID handler
    private void getMessagesByIDHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        messageService.getMessageByID(id).ifPresentOrElse(ctx::json, () -> ctx.json(""));
    }


    // Delete message handler
    private void deleteMessageHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ctx.status(200).json(messageService.deleteMessage(id) ? "Deleted" : "");
    }


    // Update message handler
    private void updateMessageHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        String newText = ctx.body();
        ctx.status(messageService.updateMessage(id, newText) ? 200 : 400);
    }


    // Get messages handler
    private void getMessagesByUserHandler(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByUser(userId));
    }

}