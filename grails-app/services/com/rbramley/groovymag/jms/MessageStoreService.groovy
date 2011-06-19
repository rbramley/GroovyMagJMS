package com.rbramley.groovymag.jms

import grails.plugin.jms.*
import java.util.Map

class MessageStoreService {

	def jmsService
	
    static transactional = true
    static exposes = ['jms']

    @Queue(name='msg.new')
    def createMessage(msg) {
		sleep(2000) // slow it down
		
        def messageInstance = new Message(body:msg)
        if (messageInstance.save(flush: true)) {
            log.info "Saved message: id = ${messageInstance.id}"
			
			// publish an event
			jmsService.send(topic:'msgevent', [id:messageInstance.id, body:messageInstance.body])
        } else {
            log.warn 'Could not save message'
        }
		
        // explicitly return null to prevent unwanted replyTo queue attempt
        return null
    }

    @Queue(name='msg.update')
    def updateMessage(msg) {
        def messageInstance
        if(msg instanceof Map) {
            messageInstance = Message.get(msg.id)
            if(messageInstance) {
                messageInstance.body = msg.body
                if (!messageInstance.hasErrors() && messageInstance.save(flush: true)) {
                    log.info "Saved message: id = ${messageInstance.id}"
                } else {
                    log.warn 'Could not update message'
                }
            } else {
                log.warn "No message instance for ID ${msg.id}"
            }
        } else {
            log.warn "Could not determine what to do with ${msg}"
        }

        // explicitly return null to prevent unwanted replyTo queue attempt
        return null
    }
}
