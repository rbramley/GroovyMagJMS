
<%@ page import="com.rbramley.groovymag.jms.Message" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'message.label', default: 'Message')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <atmosphere:resources/>
		<script type="text/javascript">
        $(document).ready(function() {
                // jquery.atmosphere.response
                function callback(response) {
                    if (response.status == 200) {
                        var data = response.responseBody;
                        if (data.length > 0) {
                            try {
	                            var msgObj = jQuery.parseJSON(data);
	                            if (msgObj.id > 0) {
		                            var row = '<tr><td>' + msgObj.id + '</td><td>' + msgObj.body + '</td><td></td></tr>'
		                            $('tbody').append(row);
	                        	}
                            } catch (e) {
                                // Atmosphere sends commented out data to WebKit based browsers
                            }
                        }
                    }
                }

                var location = 'http://localhost:8080/GroovyMagJMS/atmosphere/messages';
                $.atmosphere.subscribe(location, callback, $.atmosphere.request = {transport: 'websocket'});				
        });
		</script>

    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'message.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="body" title="${message(code: 'message.body.label', default: 'Body')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'message.dateCreated.label', default: 'Date Created')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${messageInstanceList}" status="i" var="messageInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${messageInstance.id}">${fieldValue(bean: messageInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: messageInstance, field: "body")}</td>
                        
                            <td><g:formatDate date="${messageInstance.dateCreated}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${messageInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
