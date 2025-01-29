package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;
import java.util.Optional;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }


    // Create message
    public Message createMessage(Message message) {
        if(message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }
        return messageDAO.createMessage(message);
    }


    // Get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }


    // Get messages by ID
    public Optional<Message> getMessageByID(int messageId) {
        return messageDAO.getMessageByID(messageId);
    }


    // Delete message
    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }


    // Update message
    public boolean updateMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }


    // Get message by user
    public List<Message> getMessageByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}
