package com.rbramley.groovymag.jms

class MessageController {

    def jmsService
    def scaffold = true

    def save = {
        jmsService.send(queue:'msg.new', params.body)
        redirect(action: "list")
    }

    def update = {
        jmsService.send(queue:'msg.update', [id:params.id, body:params.body])
        flash.message = "Message queued for update"
        redirect(action: "list")
    }
}
