package grails.plugins.correios.rastreamento

class Evento {
	String tipo
	Integer status
	Date data
	String descricao
	String recebedor
	String documento
	String comentario
	String local
	String codigo
	String cidade
	String uf
	String sto
	
	static def createFromXml(xml) {
		def evt=new Evento()
		evt.parseXml(xml)
	}
	
	def parseXml(xml) {
		def df=new java.text.SimpleDateFormat('dd/MM/yyyy HH:ss')
		tipo=xml.tipo.toString()
		status=xml.status.toInteger()
		def ds="${xml.data.toString()} ${xml.hora.toString()}"
		data=df.parse(ds)
		descricao=xml.descricao.toString().trim()
		recebedor = xml.recebedor.toString().trim()
		documento = xml.documento.toString().trim()
		comentario = xml.comentario.toString().trim()
		local = xml.local.toString().trim()
		codigo = xml.codigo.toString().trim()
		cidade = xml.cidade.toString().trim()
		uf = xml.uf.toString().trim()
		sto = xml.sto.toString().trim()
		this
	}
	
	Boolean isEntregue() {
		(tipo in ['BDE','BDI','BDR'] && status==1)
	} 
	
	Boolean isFinalizado() {
		(tipo in ['BDE','BDI','BDR'] && status in [1,9,12,28,51,69]) || 
		(tipo in ['BDE','BDR'] && status in [31,43,50]) ||
		(tipo in ['BDI','BDR'] && status in [44,52]) 
	}
	
	String toString() {
		def df=new java.text.SimpleDateFormat('dd/MM/yyyy HH:mm')
		"${df.format(data)} $descricao ${finalizado ? (entregue ? '(ENTREGUE)' : '(COM PROBLEMA)') : ''}"
	}
}
