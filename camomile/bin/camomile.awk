function errorExit(message) {
  print "#####"
  print "ERROR: " message
  print "#####"
  exit 1
}

function errorMessage(message) {
  print "#######"
  print "MESSAGE: " message
  print "#######"
}

function JSONToWebappConfig(  _allLines, _jsonData, _connections, _con, _item, _driver, _url, _user, _password, _noUser, _noPassword, _allowSql, _jettyWebXml, _webXml, _camomileJettyWebXml, _camomileWebXml, _connectionsJson) {
  _connectionsJson = ".." S "connections.json"
  _jettyWebXml = ".." S "server" S "webapps" S "camomile" S "WEB-INF" S "jetty-web.xml"
  _camomileJettyWebXml = ".." S "server" S "webapps" S "camomile" S "WEB-INF" S "camomile-jetty-web.xml"
  _webXml = ".." S "server" S "webapps" S "camomile" S "WEB-INF" S "web.xml"
  _camomileWebXml = ".." S "server" S "webapps" S "camomile" S "WEB-INF" S "camomile-web.xml"

  # Read JSON file
  while ((getline line < _connectionsJson) > 0) {
    _allLines = (_allLines == "" ? "" : (_allLines "\n")) line
  }

  # Parse JSON
  ParseJSON(_allLines, _jsonData)

  # print FormatJSON(_jsonData,1)

  # Process parsed JSON
  JSONObjectMembers(_jsonData, "", _connections)

  _noUser = "false"
  _noPassword = "false"
  # Read camomile-jetty-web.xml and write jetty-web.xml file
  while ((getline line < _camomileJettyWebXml) > 0) {
    if (line ~ /<\/Configure>/) {
      for (_item in _connections) {
        _con = _connections[_item]

        if ((_con SUBSEP "driver") in _jsonData) {
          _driver = _jsonData[_con SUBSEP "driver"]
        } else {
          errorExit("No \"driver\" element specified in the connections.json file.")
        }

        if ((_con SUBSEP "url") in _jsonData) {
          _url = _jsonData[_con SUBSEP "url"]
        } else {
          errorExit("No \"url\" element specified in the connections.json file.")
        }

        if ((_con SUBSEP "user") in _jsonData) {
          _user = _jsonData[_con SUBSEP "user"]
        } else {
          _noUser = "true"
          errorMessage("No \"user\" element specified in the connections.json file, this is fine as long as they are specified in the \"url\".")
        }

        if ((_con SUBSEP "password") in _jsonData) {
          _password = _jsonData[_con SUBSEP "passowrd"]
          if (_noUser == "true") {
            errorExit("No \"user\" element specified in the connections.json file, but a \"password\" element has been specified.")
          }
        } else {
          _noPassword = "true"
          if (_noUser == "true") {
            errorMessage("No \"password\" element specified in the connections.json file, this is fine as long as they are specified in the \"url\".")
          } else {
            errorExit("No \"password\" element specified in the connections.json file, but a \"user\" element has been specified, if you want a blank password just use a blank string \"\" for the value of the \"password\" element.")
          }
        }

        print "    <New id=\"" _con "\" class=\"org.eclipse.jetty.plus.jndi.Resource\">" > _jettyWebXml
        print "        <Arg></Arg>" > _jettyWebXml
        print "        <Arg>jdbc/" _con "</Arg>" > _jettyWebXml
        print "        <Arg>" > _jettyWebXml
        print "            <New class=\"com.mchange.v2.c3p0.ComboPooledDataSource\">" > _jettyWebXml
        print "                <Set name=\"driverClass\">" _driver "</Set>" > _jettyWebXml
        print "                <Set name=\"jdbcUrl\">" _url "</Set>" > _jettyWebXml
        if (_noUser == "false") {
          print "                <Set name=\"user\">" _user "</Set>" > _jettyWebXml
        }
        if (_noPassword == "false") {
          print "                <Set name=\"password\">" _password "</Set>" > _jettyWebXml
        }
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
    while ((getline line < _camomileWebXml) > 0) {
    if (line ~ /<\/web-app>/) {
      for (_item in _connections) {
        _con = _connections[_item]

        _allowSql = _jsonData[_con SUBSEP "allow sql"]
        if (_allowSql != "true" && _allowSql != "false") {
          _allowSql = "false"
        }

        print "    <context-param>" > _webXml
        print "        <param-name>" _con ":allow sql</param-name>" > _webXml
        print "        <param-value>" _allowSql "</param-value>" > _webXml
        print "        <description>" > _webXml
        print "            Allow Raw SQL (true or false)." > _webXml
        print "        </description>" > _webXml
        print "    </context-param>" > _webXml
      }

      print "" > _webXml

      for (_item in _connections) {
        _con = _connections[_item]

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

function startJettyServer(  _cmd) {
  if (PORT != "") {
    _cmd = "cd .." S "server " CS " java -Djetty.port=" PORT " -jar start.jar lib=.." S "connectors OPTIONS=plus"
  } else {
    _cmd = "cd .." S "server " CS " java -jar start.jar lib=.." S "connectors OPTIONS=plus"
  }

  system(_cmd)
}

BEGIN {
  if (OS == "WINDOWS") {
    S = "\\"
    CS = "&"
  } else {  
    S = "/"
    CS = ";"
  }

  JSONToWebappConfig()
  startJettyServer()
}
