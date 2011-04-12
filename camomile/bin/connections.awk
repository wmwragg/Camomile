function JSONToWebappConfig(  _allLines, _jsonData, _connections, _con, _item, _jettyWebXml, _webXml) {
    # Read JSON file
    while ((getline line < "../connections.json") > 0)
    {
        _allLines = (_allLines == "" ? "" : (_allLines "\n")) line
    }

    # Parse JSON
    ParseJSON(_allLines, _jsonData)

    # print FormatJSON(_jsonData,1)

    # Process parsed JSON
    JSONObjectMembers(_jsonData, "", _connections)

    # Read camomile-jetty-web.xml and write jetty-web.xml file
    _jettyWebXml = "../server/webapps/camomile/WEB-INF/jetty-web.xml"
    while ((getline line < "../server/webapps/camomile/WEB-INF/camomile-jetty-web.xml") > 0) {
        if (line ~ /<\/Configure>/) {
            for (_item in _connections) {
                _con = _connections[_item]

                #print _con ".driver = "_jsonData[_con SUBSEP "driver"]
                #print _con ".url = " _jsonData[_con SUBSEP "url"]
                #print _con ".user = " _jsonData[_con SUBSEP "user"]
                #print _con ".password = " _jsonData[_con SUBSEP "password"]

                print "    <New id=\"" _con "\" class=\"org.eclipse.jetty.plus.jndi.Resource\">" > _jettyWebXml
                print "        <Arg></Arg>" > _jettyWebXml
                print "        <Arg>jdbc/" _con "</Arg>" > _jettyWebXml
                print "        <Arg>" > _jettyWebXml
                print "            <New class=\"com.mchange.v2.c3p0.ComboPooledDataSource\">" > _jettyWebXml
                print "                <Set name=\"driverClass\">" _jsonData[_con SUBSEP "driver"] "</Set>" > _jettyWebXml
                print "                <Set name=\"jdbcUrl\">" _jsonData[_con SUBSEP "url"] "</Set>" > _jettyWebXml
                print "                <Set name=\"user\">" _jsonData[_con SUBSEP "user"] "</Set>" > _jettyWebXml
                print "                <Set name=\"password\">" _jsonData[_con SUBSEP "password"] "</Set>" > _jettyWebXml
                print "            </New>" > _jettyWebXml
                print "        </Arg>" > _jettyWebXml
                print "    </New>" > _jettyWebXml
            }
            print "</Configure>" > _jettyWebXml
        } else {
            print line > _jettyWebXml
        }
    }
    close(_jettyWebXml)

    # Read camomile-web.xml and write web.xml file
    _webXml = "../server/webapps/camomile/WEB-INF/web.xml"
    while ((getline line < "../server/webapps/camomile/WEB-INF/camomile-web.xml") > 0) {
        if (line ~ /<\/web-app>/) {
            for (_item in _connections) {
                _con = _connections[_item]

                #print _con "[\"allow sql\"] = " _jsonData[_con SUBSEP "allow sql"]

                print "    <resource-ref>" > _webXml
                print "        <description>Camomile DataSource Resource</description>" > _webXml
                print "        <res-ref-name>jdbc/" _con "</res-ref-name>" > _webXml
                print "        <res-type>javax.sql.DataSource</res-type>" > _webXml
                print "        <res-auth>Container</res-auth>" > _webXml
                print "    </resource-ref>" > _webXml
            }
            print "</web-app>" > _webXml
        } else {
            print line > _webXml
        }
    }
    close(_webXml)

}

BEGIN {
    JSONToWebappConfig()
}
