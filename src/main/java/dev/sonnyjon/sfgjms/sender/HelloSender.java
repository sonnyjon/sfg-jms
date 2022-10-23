package dev.sonnyjon.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sonnyjon.sfgjms.config.JmsConfig;
import dev.sonnyjon.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

/**
 * Created by Sonny on 10/17/2022.
 */
@RequiredArgsConstructor
@Component
public class HelloSender
{
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage()
    {
        HelloWorldMessage message = HelloWorldMessage.builder()
                                            .id( UUID.randomUUID() )
                                            .message( "Hello World!" )
                                            .build();

        jmsTemplate.convertAndSend( JmsConfig.MY_QUEUE, message );
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException
    {
        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();

        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {

            Message helloMessage;

            try {
                helloMessage = session.createTextMessage(objectMapper.writeValueAsString( message ));
                helloMessage.setStringProperty("_type", "dev.sonnyjon.sfgjms.model.HelloWorldMessage");

                System.out.println("Sending Hello");
                return helloMessage;
            }
            catch (JsonProcessingException e) {
                throw new JMSException("boom");
            }
        });

        if (receivedMsg != null) System.out.println(receivedMsg.getBody(String.class));
    }
}