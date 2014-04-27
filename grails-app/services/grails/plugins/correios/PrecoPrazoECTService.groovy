package grails.plugins.correios
class PrecoPrazoECTService {
	static transactional = false
	def grailsApplication
	
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

			withSoap(serviceURL: 'http://ws.correios.com.br/calculador/CalcPrecoPrazo.asmx') {
				def result = send(SOAPAction: 'http://tempuri.org/CalcPrecoPrazo') {
				      body {
					 CalcPrecoPrazo(xmlns: 'http://tempuri.org/') {
					    nCdEmpresa(codigoEmpresa)
					    sDsSenha(senha)
					    nCdServico(codigoServico)
					    sCepOrigem(cepOrigem)
					    sCepDestino(cepDestino)
					    nVlPeso(speso)
					    nCdFormato(embalagem)
		                            nVlComprimento(comp)
					    nVlAltura(altr)
					    nVlLargura(larg)
					    nVlDiametro(diam)
					    sCdMaoPropria(maoPropria)
					    nVlValorDeclarado(valorDeclarado)
					    sCdAvisoRecebimento(avisoRecebimento)
					 }
				      }
				   }
				
				def resp= result.CalcPrecoPrazoResponse.CalcPrecoPrazoResult.Servicos.cServico[0]
				log.debug "Resposta: ${resp}"
				if (resp.Erro && resp.Erro!='0') {
					log.debug "Resposta com erro: ${resp.Erro} - ${resp.MsgErro}"
					throw new RuntimeException("Erro: ${resp.Erro} Msg:${resp.MsgErro}")
				} else {
					r=[valor:resp.Valor,prazoEntrega:resp.PrazoEntrega]
					addToCache(args,r)
					r
				}
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
