// Place your Spring DSL code here
beans = {
    jmsConnectionFactory(org.apache.activemq.ActiveMQConnectionFactory) {
        brokerURL = 'vm://localhost'
    }
}
