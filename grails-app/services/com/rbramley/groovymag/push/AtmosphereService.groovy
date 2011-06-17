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
            payload = msg.encodeAsJSON()
        }
		
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
		log.info "onStateChange, message: ${event.message}"
	
		if (!event.message) return
		
		if (event.isResuming() || event.isResumedOnTimeout()) {
			// TODO 
		} else {
			event.resource.response.writer.with {				
				write "<script>parent.callback('${event.message}');</script>"				
				flush()
			}
			event.resume()
		}
	}
}
