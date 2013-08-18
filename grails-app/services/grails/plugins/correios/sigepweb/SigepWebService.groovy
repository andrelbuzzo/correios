package grails.plugins.correios.sigepweb

import groovyx.net.ws.WSClient

class SigepWebService {
	def grailsApplication
	
	
	def getWsdlURL() {
		grailsApplication.config.grails.plugins.correios.sigepweb.wsdl
	}
	def getUser() {
		grailsApplication.config.grails.plugins.sigepweb.user
	}
	
	def getPassword() {
		grailsApplication.config.grails.plugins.sigepweb.password
	}
	
	def verificaDisponibilidadeServico(codAdministrativo, numeroServico, cepOrigem, cepDestino){
		def proxy=new WSClient(wsdlURL)
		proxy.verificaDisponibilidadeServico(codAdministrativo, numeroServico, cepOrigem, cepDestino, user,password)
	}
	
	def buscaCliente (idContrato, idCartaoPostagem) {
		def proxy=new WSClient(wsdlURL)
		proxy.buscaCliente (idContrato, idCartaoPostagem, user, password)
	}
	
	def consultaCEP(cep) {
		def proxy=new WSClient(wsdlURL)
		proxy.consultaCEP(cep)
	}

	def getStatusCartaoPostagem (numeroCartaoPostagem) {
		def proxy=new WSClient(wsdlURL)
		proxy.StatusCartaoPostagem (numeroCartaoPostagem, user, password)
	}

	
	def solicitaEtiquetas(tipoDestinatario, identificador, idServico, qtdEtiquetas) {
		def proxy=new WSClient(wsdlURL)
		proxy.solicitaEtiquetas(tipoDestinatario, identificador, idServico,	qtdEtiquetas,  user, password)
	}
	def geraDigitoVerificadorEtiquetas(etiquetas) {
		def proxy=new WSClient(wsdlURL)
		proxy.geraDigitoVerificadorEtiquetas(etiquetas,user,password)
	}
	
}
