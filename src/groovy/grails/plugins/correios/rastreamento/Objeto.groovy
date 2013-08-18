package grails.plugins.correios.rastreamento

class Objeto {
	String numero
	List<Evento> eventos=[]
	
	def parseXml(xml) {
		this.numero=xml.numero.toString()
		def le=xml.evento
		le.each { e->
			def evt=new Evento()
			evt.parseXml(e)
			this.eventos.add(evt)
		}
		this
	}
	
	static Objeto createFromXml(xml) {
		def obj=new Objeto()
		obj.parseXml(xml)
		obj
	}
	
	String toString() {
		"$numero : ${eventos}"
	}
}
