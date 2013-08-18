// configuration for plugin testing - will not be included in the plugin zip

development {
	grails.plugins.correios.scol.wsdl='http://webservicescolhomologacao.correios.com.br/ScolWeb/WebServiceScol?wsdl'
	grails.plugins.correios.sigepweb.wsdl='http://sigep.correios.com.br/sigep/update/AtendeClienteService.wsdl'
	grails.plugins.correios.precoPrazo.wsdl='http://ws.correios.com.br/calculador/CalcPrecoPrazo.asmx?WSDL'
	grails.plugins.correios.rastreamento.url='http://websro.correios.com.br/sro_bin/sroii_xml.eventos'

	grails.plugins.correios.usuario='60618043'
	grails.plugins.correios.senha='8o8otn'
	grails.plugins.correios.codigoAdministrativo='5122864'
	grails.plugins.correios.contrato='1000010572'
	grails.plugins.correios.codigoServico='41076'
	grails.plugins.correios.cartao='0057702519'
}
production {
	grails.plugins.sigepweb.scol.wsdl='http://webservicescol.correios.com.br/ScolWeb/WebServiceScol?wsdl'
	grails.plugins.correios.sigepweb.wsdl='http://sigep.correios.com.br/sigep/update/AtendeClienteService.wsdl'
	grails.plugins.correios.precoPrazo.wsdl='http://ws.correios.com.br/calculador/CalcPrecoPrazo.asmx?WSDL'
	grails.plugins.correios.rastreamento.url='http://websro.correios.com.br/sro_bin/sroii_xml.eventos'
}

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}
