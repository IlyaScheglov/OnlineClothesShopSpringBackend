package com.example.NewProject.services;

import com.example.NewProject.entities.Orders;
import com.example.NewProject.entities.Users;
import lombok.RequiredArgsConstructor;
import org.hibernate.Transaction;
import org.hibernate.query.sqm.function.SelfRenderingOrderedSetAggregateFunctionSqlAstExpression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class SendingEmails {

    private final StatusesService statusesService;

    private final String password = "XXX";
    private final String from = "XXX";
    private final String host = "smtp.gmail.com";
    private final String smtpPort = "465";
    public void sendMailToUser(Users user, Orders order, int type){
        String to = user.getEmail();
        Session session = getSession();

        try{
            String messegeToUser = makeTxtByType(type, order);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Новое сообщение от магазина shoMa");
            message.setText(messegeToUser);
            Transport.send(message);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private Session getSession(){

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                }
        );
        return session;
    }

    private String makeTxtByType(int type, Orders order){

        StringBuilder builder = new StringBuilder();

        if (type == 1){
            builder.append("Здравствуйте, вы оформили заказ!\n\n");
            builder.append("Номер заказа: " + order.getId() + ".\n");
            builder.append("Сумма заказа: " + String.valueOf(order.getCost()) + "₽.\n");
            builder.append("Указанный адрес: " + order.getAddress() + ".\n");
            builder.append("Статус заказа: " + statusesService.getStatusById(order.getStatusId()) + ".\n\n");
            builder.append("Если у вас остались вопросы, пишите нашему администратору в telegram: @Gendefo!");
        }
        else if (type == 2){
            builder.append("Здравствуйте, ваш заказ отправлен!\n\n");
            builder.append("Номер заказа: " + order.getId() + ".\n");
            builder.append("Сумма заказа: " + String.valueOf(order.getCost()) + "₽.\n");
            builder.append("Адрес отделения СДЭК, куда прийдет заказ: " + order.getFinalAddress() + ".\n");
            builder.append("Статус заказа: " + statusesService.getStatusById(order.getStatusId()) + ".\n\n");
            builder.append("Если у вас остались вопросы, пишите нашему администратору в telegram: @Gendefo!");
        }
        else if (type == 3){
            builder.append("Здравствуйте, пришел! Вы можете забрать его отделении СДЭК по номеру телефона!\n\n");
            builder.append("Номер заказа: " + order.getId() + ".\n");
            builder.append("Сумма заказа: " + String.valueOf(order.getCost()) + "₽.\n");
            builder.append("Адрес отделения СДЭК: " + order.getFinalAddress() + ".\n");
            builder.append("Статус заказа: " + statusesService.getStatusById(order.getStatusId()) + ".\n\n");
            builder.append("Если у вас остались вопросы, пишите нашему администратору в telegram: @Gendefo!");
        }

        return builder.toString();
    }
}
