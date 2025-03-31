package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import DAO.AccountDAO;
import DAO.MessageDAO;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService(new AccountDAO());
        this.messageService = new MessageService(new MessageDAO(), new AccountDAO());
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonProcessingException {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account newAccount = accountService.register(account);
            if (newAccount == null) {
                ctx.status(400);
            } else {
                ctx.json(newAccount);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            if (loggedInAccount == null) {
                ctx.status(401);
            } else {
                ctx.json(loggedInAccount);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        try {
            Message message = ctx.bodyAsClass(Message.class);
            Message newMessage = messageService.create(message);
            if (newMessage == null) {
                ctx.status(400);
            } else {
                ctx.json(newMessage);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message == null) {
            ctx.status(200).result("");
        } else {
            ctx.json(message);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessageById(message_id);

            if (deletedMessage == null) {
                ctx.status(200).result("");
            } else {
                ctx.status(200).json(deletedMessage);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    private void updateMessageByIdHandler(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            String newMessage = ctx.bodyAsClass(Message.class).getMessage_text();
            Message updatedMessage = messageService.updateMessageById(message_id, newMessage);

            if (updatedMessage == null) {
                ctx.status(400);
            } else {
                ctx.json(updatedMessage);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    private void getMessagesByUserIdHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByUserId(account_id));
    }
}