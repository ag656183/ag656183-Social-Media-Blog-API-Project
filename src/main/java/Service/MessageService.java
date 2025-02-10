package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        // Validation: message text must not be blank or exceed 255 characters
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || 
            message.getMessage_text().length() > 255) {
            return null;
        }

        // Validation: posted_by must refer to an existing user
        if (!messageDAO.doesUserExist(message.getPosted_by())) {
            return null;
        }

        // Save message and return the created object
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
}
