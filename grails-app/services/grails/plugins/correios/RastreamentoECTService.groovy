package grails.plugins.correios

import groovyx.net.http.*
import grails.plugins.correios.rastreamento.*

class RastreamentoECTService {
	static transactional=false
	
	def urlBase='http://websro.correios.com.br'
	def grailsApplication

	
	def getUsuario() {
		grailsApplication.config.grails.plugins.correios.usuario
	}
	def getSenha() {
		grailsApplication.config.grails.plugins.correios.senha
	}
	
	def consultar(lobj,resultado=TipoResultado.TODOS,u=null,p=null) {
		def user= u ?: usuario
		def pass= p ?: senha
		def objetos=lobj.join()
		def http = new HTTPBuilder()
		def data = [Usuario:user, Senha:pass,Tipo:'L',Resultado:resultado.codigo,Objetos:objetos]
		log.debug "Consultado SRO User: $user Objs.: $lobj"
		http.request(urlBase, Method.POST, ContentType.XML) { req ->
					uri.path = '/sro_bin/sroii_xml.eventos'
					headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
					uri.query = data
					response.success = { resp, xml ->
						log.debug "Versao: ${xml.versao} Qtde: ${xml.qtd} "
						def lobjetos=[]
						def lo=xml.objeto
						lo.each { o->
							def obj=Objeto.createFromXml(o)
							lobjetos.add(obj)
						}
						lobjetos
					}
		
					response.'404' = {resp, xml->
						log.debug "SGWS: confirmarAtivacao : not found: ${xml}"
						throw new RuntimeException("ERRO 404",xml)
					}
					response.'500' = {resp, xml ->
						log.debug "SGWS: confirmarAtivacao : 500 : ${xml}"
						throw new RuntimeException("ERRO 500",xml)
					}
					response.failure = { resp ->
						log.debug "Error: $resp"
					}
				}
	}

}
