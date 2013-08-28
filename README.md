correios
========

Access to various Web Services of Correios from Brazil 

- Preços e Prazos


- Rastreamento (Tracking)
Service: rastreamentoECTService
Methods:
	consultar(lobj,tipo[,usuario][,senha])
	List<String> lobj - lista de identificadores (p.ex. ['PG747937788BR','SW091892778BR'])
	tipo - tipo de consulta:
		grails.plugins.correios.rastreamento.TipoResultado.TODOS - return all events
		grails.plugins.correios.rastreamento.TipoResultado.ULTIMO - return only the last one
	usuario/senha - user and password informed by Correios 

Configuration

You can configure some parameters in the grails-app/conf/Config.groovy. They are:


	grails.plugins.correios.xxxx.usuario='*******' 		// User provided by Correios, 
								// in development and test environments, for tracking tests, 
								// you can use the "ECT" user with "SRO" password
	grails.plugins.correios.xxxx.senha='******'			// Password provided by Correios
	grails.plugins.correios.xxxx.codigoAdministrativo='*******'  // Administrative code, provided by Correios
	grails.plugins.correios.xxxx.contrato='*********'		// Contract id, provided by correios


Where 'xxxx' is the service:
	sro - Serviço de Rastreamento de Objetos
	sigepweb - Sistema de gerenciamento de endereçamento Postal WEB
	scop
	
