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


    // Register User
    private void registerUser(Context ctx) {
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
    private void loginUser(Context ctx) {
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
    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Optional<Message> createdMessage = messageService.createMessage(message);

        if(createdMessage.isPresent()) {
            ctx.json(createdMessage.get());
        }
        else {
            ctx.status(400);
        }
    }


    // Get all messages
    private void getAllMessages(Context ctx) {
            List<Message> messages = messageService.getAllMessages();
            ctx.json(messages);
    }


    // Get messages by ID
    private void getMessageByID(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Optional<Message> message = messageService.getMessageByID(messageID);

        if(message.isPresent()) {
            ctx.json(message.get());

        }
    }


    // Delete message
    private void deleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message newMessageData = ctx.bodyAsClass(Message.class);
        Optional<Message> updatedMessage = messageService.updatedMessage(messageId, newMessageData);

        if(updatedMessage.isPresent()) {
            ctx.json(updatedMessage.get());
        }
        else {
            ctx.status(400);
        }
    }


    // Get message by user
    private void getMessageByUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessageByUser(accountId);
        ctx.json(messages);
    }

}