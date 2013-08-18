package grails.plugins.correios

class PrecoPrazoECTService {
	static transactional = false
	def grailsApplication
	def webService
	
	def proxyInstance
	
	def getWsdlURL() {
		grailsApplication.config.grails.plugins.correios.precoPrazo.wsdl
	}
	
	def getProxy(){
		if (!proxyInstance)
			proxyInstance = webService.getClient(wsdlURL)
		proxyInstance
	}
	
	static def cache = [:]
	static def cacheTimeOutValue
	
	def calcPrecoPrazo(String codigoEmpresa,String senha,String codigoServico,
				String cepOrigem,String cepDestino,String speso,
				Integer embalagem,Double comp,Double altr,Double larg,Double diam,String maoPropria,Double valorDeclarado,String avisoRecebimento) {
			log.debug "Sending request to Correios ws..."
			
			def args=[codigoEmpresa,senha,codigoServico,cepOrigem,cepDestino,speso,embalagem,comp,altr,larg,diam,maoPropria,valorDeclarado,avisoRecebimento]
			def r=getFromCache(args)
			if (r) 
				return r
			def result=proxy.CalcPrecoPrazo(codigoEmpresa,senha,codigoServico,cepOrigem,cepDestino,speso,embalagem,comp,altr,larg,diam,maoPropria,valorDeclarado,avisoRecebimento)
			def resp=result.servicos.cServico[0]
			log.debug "Resposta: ${resp}"
			if (resp.erro && resp.erro!='0') {
				log.debug "Resposta com erro: ${resp.erro} - ${resp.msgErro}"
				throw new RuntimeException("Erro: ${resp.erro} Msg:${resp.msgErro}")
			} else {
				r=[valor:resp.valor,prazoEntrega:resp.prazoEntrega]
				addToCache(args,r)
				r
			}
		
	}
	def getFromCache(args) {
		def hash=String.format("%08x", args.hashCode())
		def item=cache[hash]
		if (item) {
			def today=new Date()
			if ((today - item.date) > cacheTimeOut) {
				cache[hash]=null
				return null
			} else return item.result
		} else return null
	}
	
	def addToCache(args,result) {
		def hash=String.format("%08x", args.hashCode())
		def item=[result:result,args:args,date:new Date()]
		cache[hash]=item
		item
	}
}
