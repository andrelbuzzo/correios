package grails.plugins.correios

import groovyx.net.http.*
import grails.plugins.correios.rastreamento.*

class RastreamentoECTService {
	static transactional=false
	
	def urlBase='http://websro.correios.com.br'
	def grailsApplication

	static final String TEST_USER = 'ECT'
	static final Integer NUMMAX_OBJECTS = 50
	
	
	def getUsuario() {
		grailsApplication.config.grails.plugins.correios.sro.usuario
	}
	def getSenha() {
		grailsApplication.config.grails.plugins.correios.sro.senha
	}
	
	def consultar(lobj,resultado=TipoResultado.TODOS,u=null,p=null) {
		def user= u ?: usuario
		def pass= p ?: senha
		def llc=[]
		if (user=='TEST_USER') {
			llc=partition(lobj,2)
		} else {
			llc=partition(lobj,NUMMAX_OBJECTS)
		}
		log.debug "Tracking list: $llc"
		def lobjetos=[]
		llc.each { lo->
			def objs=consultarObjetos(lo,resultado,user,pass)
			lobjetos+=objs
		}
		lobjetos
	}
	private def consultarObjetos(lobj,resultado,user,pass) {
			def objetos=lobj.join()
			def http = new HTTPBuilder()
			def data = [Usuario:user, Senha:pass,Tipo:'L',Resultado:resultado.codigo,Objetos:objetos]
			log.debug "Consultado SRO User: $user/$pass Objs.: $lobj"
			http.request(urlBase, Method.POST, ContentType.XML) { req ->
					uri.path = '/sro_bin/sroii_xml.eventos'
					headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
					uri.query = data
					response.success = { resp, xml ->
						log.debug "Versao: ${xml.versao} Qtde: ${xml.qtd} "
						def lobjetos=[]
						if (xml.error.size()>0 && xml.error.toString().toLowerCase()!='objetos não encontrados') {
							throw new RuntimeException("SRO Error: ${xml.error.toString()}")
						} else if (xml.error.size()>0 && xml.error.toString().toLowerCase()=='objetos não encontrados') {
							return []
						}
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
	private def partition(array, size) {
		def partitions = []
		int partitionCount = array.size() / size
	
		partitionCount.times { partitionNumber ->
			def start = partitionNumber * size
			def end = start + size - 1
			partitions << array[start..end]
		}
	
		if (array.size() % size) partitions << array[partitionCount * size..-1]
		return partitions
	}
}
