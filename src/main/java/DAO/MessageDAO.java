package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message insertMessage(Message message) {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int messageId = rs.getInt(1);
                    return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Message creation failed
    }

    public boolean doesUserExist(int userId) {
        String sql = "SELECT COUNT(*) FROM Account WHERE account_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
    
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Message not found
    }

    public Message deleteMessageById(int messageId) {
        String selectSql = "SELECT * FROM Message WHERE message_id = ?";
        String deleteSql = "DELETE FROM Message WHERE message_id = ?";
    
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
    
            // First, retrieve the message to return it after deletion
            selectStmt.setInt(1, messageId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                Message deletedMessage = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
    
                // Now, delete the message
                deleteStmt.setInt(1, messageId);
                deleteStmt.executeUpdate();
                
                return deletedMessage; // Return the deleted message
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Message not found or deletion failed
    }

    public Message updateMessageText(int messageId, String newText) {
        String updateSql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        String selectSql = "SELECT * FROM Message WHERE message_id = ?";
    
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
    
            // Validate if message exists
            selectStmt.setInt(1, messageId);
            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) {
                return null; // Message not found
            }
    
            // Update message text
            updateStmt.setString(1, newText);
            updateStmt.setInt(2, messageId);
            int affectedRows = updateStmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Return updated message
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        newText,
                        rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Update failed
    }

    public List<Message> getMessagesByUser(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
    
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
}