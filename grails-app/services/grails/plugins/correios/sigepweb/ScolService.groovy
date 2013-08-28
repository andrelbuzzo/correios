package grails.plugins.correios.sigepweb

import groovyx.net.ws.WSClient

class ScolService {
	def grailsApplication
	
	private def proxy_
	
	def getProxy() {
		if (!proxy_) {
			proxy_=new WSClient(wsdlURL)
			proxy_.initialize()
		}
	}

	def getWsdlURL() {
		grailsApplication.config.grails.plugins.sigepweb.scol.wsdl
	}
	def getUsuario() {
		grailsApplication.config.grails.plugins.sigepweb.scol.usuario
	}
	def getSenha() {
		grailsApplication.config.grails.plugins.sigepweb.scol.senha
	}
	def getCodigoAdministrativo() {
		grailsApplication.config.grails.plugins.sigepweb.scol.codigoAdministrativo
	}
	
	def getContrato() {
		grailsApplication.config.grails.plugins.sigepweb.scol.contrato
	}
	
	def getCodigoServico() {
		grailsApplication.config.grails.plugins.sigepweb.scol.codigoServico
	}
	
	def cartao() {
		grailsApplication.config.grails.plugins.sigepweb.scol.cartao
	}
	
	def solicitaRange(tipo,quantidade) {
		proxy.solicitaRange(usuario,senha,codigoAdministrativo,contrato,tipo,quantidade)
	}
	
	def calcularDigitoVerificador(numero) {
		proxy.calcularDigitoVerificador(usuario,senha,codigoAdministrativo,numero)
	}
	
	


}
