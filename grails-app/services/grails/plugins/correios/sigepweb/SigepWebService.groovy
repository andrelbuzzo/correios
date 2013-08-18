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
	
	def gerarArquivo(data,clos=null) {
		def text="1SIGEP DESTINATARIO NACIONAL\r\n"
		text+=data.collect{ r->
			formatDestinatario(r,clos)
		}.join("\r\n")
		text+=String.format("\r\n9%06d\r\n",data.size())
		text
	}
	
	private def formatCpf(c) {
		def cpf=c.replaceAll('\\.','').replaceAll('-','').replaceAll('/','')
		if (cpf) while(cpf.size()<11) cpf='0'+cpf
		cpf
	}
	
	private def formatDestinatario(l,cls) {
		def fields=['cpf': [format:{ formatCpf(it) }],
					'nome': [format:{ it.size()<50 ? it : it.substring(0,50) },required:true],
					'email': [format:{ it.size()<50 ? it : it.substring(0,50) }],
					'ac': [format:{ it.size()<50 ? it : it.substring(0,50) }],
					'contato': [format:{ it.size()<50 ? it : it.substring(0,50) }],
					'cep': [format:{ it.replaceAll('\\.','').replaceAll('-','')},required:true],
					'logradouro': [format:{ it.size()<50 ? it : it.substring(0,50) },required:true],
					'numero': [required:true],
					'complemento': [format:{ it.size()<30 ? it : it.substring(0,30) }],
					'bairro': [format:{ it.size()<50 ? it : it.substring(0,50) }],
					'cidade': [format:{ it.size()<50 ? it : it.substring(0,50) },required:true],
					'telefone': [:],
					'celular': [:],
					'fax': [:]]
					
		def r=[:]
		if (cls) {
			r=cls(l)
		} else r=l
		
		def m=[:]
		
		fields.each { k,v->
			if (r[k]) {
				def val=r[k]
				if (v.format) val=v.format(val)
				m[k]=val
			} else if (v.required) throw new RuntimeException("Field $k not found for register $l")
			else m[k]=''
		}
		
		def result=String.format("2%14s%-50s%-50s%-50s%-50s%8s%-50s%6s%-30s%-50s%-50s%18s%12s%12s",m.cpf,m.nome,m.email,m.ac,m.contato,
					 m.cep,m.logradouro,m.numero,m.complemento,m.bairro,m.cidade,
					 m.telefone,m.celular,m.fax)
		result
	}
}
