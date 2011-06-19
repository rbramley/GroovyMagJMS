package com.rbramley.groovymag.push

import grails.plugin.jms.*

class AtmosphereService {

    static transactional = false

    static atmosphere = [mapping: '/atmosphere/messages']

    static exposes = ['jms']

    @Subscriber(topic='msgevent')
    def onEvent(msg) {
        def payload = msg
        if(msg instanceof Map) {
            // convert map messages to JSON
            payload = msg.encodeAsJSON()
        }

        // broadcast to the atmosphere
        broadcaster['/atmosphere/messages'].broadcast(payload)

        return null
    }

    def onRequest = { event ->
        // We should only have GET requests here
        log.info "onRequest, method: ${event.request.method}"

        // Mark this connection as suspended.        
        event.suspend()
    }

    def onStateChange = { event ->
        if (event.message) {
            log.info "onStateChange, message: ${event.message}"

            if (event.isSuspended()) {
                event.resource.response.writer.with {
                    write "<script>parent.callback('${event.message}');</script>"
                    flush()
                }
                event.resume()
            }
        }
    }
}
