package ma.cdgk.integration.camel.config;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJms
public class ProxyConfiguration {

    @Value("${artemis.broker-url}")
    private String brokerUrl;

    @Value("${artemis.broker-username}")
    private String brokerUsename;

    @Value("${artemis.broker-password}")
    private String brokerPassword;

    @Bean
    public ActiveMQConnectionFactory receiverActiveMQConnectionFactory() {
        return new ActiveMQConnectionFactory(brokerUrl, brokerUsename, brokerPassword);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionAcknowledgeMode(javax.jms.Session.CLIENT_ACKNOWLEDGE);
        factory.setConnectionFactory(receiverActiveMQConnectionFactory());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(receiverActiveMQConnectionFactory());
        return jmsTransactionManager;
    }
}
