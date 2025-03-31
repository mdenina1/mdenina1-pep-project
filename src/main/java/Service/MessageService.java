package Service;

import DAO.MessageDAO;
import Model.Message;
import DAO.AccountDAO;
import java.util.List;

public class MessageService {
    private final AccountDAO accountDAO;
    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message create(Message message) {
        if (message == null) {
            return null;
        }
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255 || accountDAO.findUserById(message.getPosted_by()) == null ) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    public Message updateMessageById(int message_id, String newMessage) {
        if (newMessage == null) {
            return null;
        }
        if (newMessage.isBlank() || newMessage.length() > 255 || messageDAO.findMessageById(message_id) == null) {
            return null;
        }
        return messageDAO.updateMessage(message_id, newMessage);
    }

    public List<Message>getMessagesByUserId(int account_id) {
        return messageDAO.getMessagesByUserId(account_id);
    }
}
