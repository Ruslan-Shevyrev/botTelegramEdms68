/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botTelegramEdms68.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.botTelegramEdms68.connect.Connect;

/**
 *
 * @author shrv
 */
public class SqlQuery {

    //private final String SELECTPHONE = "select * from edms.test_telegram_table where phone = ?";
    //private final String INSERTPHONE = "UPDATE edms.test_telegram_table SET chat_id = ? where id = ?";
    private final String SELECTCHATID = "SELECT * from edms.employees where chat_id = ?";
    private final String INSERTPHONE = "UPDATE edms.employees SET chat_id = ? where mobile = ?";
    private final String SELECTCHAT = "SELECT * from edms.employees where username = ?";

    public boolean isPhone(String phone, Long chatId) {
        try (Connection con = Connect.getConnection()) {
            PreparedStatement psUpd = con.prepareStatement(INSERTPHONE);

            if (phone.charAt(0) == '+') {
                phone = phone.substring(1, phone.length());
            }
            //phone = phone.replace('-', '\0').replace(')', '\0').replace('(', '\0').replace('+','\0');
            psUpd.setLong(1, chatId);
            psUpd.setString(2, phone);
            if (psUpd.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isChatId(Long chatId) {
        try (Connection con = Connect.getConnection()) {
            PreparedStatement psSel = con.prepareStatement(SELECTCHATID);
            psSel.setLong(1, chatId);
            ResultSet rs = psSel.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String chatId(String account_name) {
        String result = null;
        try (Connection con = Connect.getConnection()) {
            PreparedStatement psSel = con.prepareStatement(SELECTCHAT);
            psSel.setString(1, account_name);
            ResultSet rs = psSel.executeQuery();
            if (rs.next()) {
                result = rs.getString("chat_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
