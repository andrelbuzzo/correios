package grails.plugins.correios.rastreamento

public enum TipoResultado {
	TODOS('T','Todos'),
	ULTIMO('U','Ultimo')
	
	
	String nome
	String codigo
	
	TipoResultado(codigo,nome) {
		this.codigo=codigo
		this.nome=nome
	}
	
	static def list() {
		[TODOS,ULTIMO]
	}
}