<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
  <title>KopiDoc</title>
  <script type="text/javascript">
/*
    var config = {
    contextPath: '${pageContext.request.contextPath}',
    sourcePath: 'src/main/java',
    classPath: '/Users/asm/.m2/repository/javax/servlet/servlet-api/2.5/servlet-api-2.5.jar:/Users/asm/Development/KopiDoc/lib/jnotify/jnotify-0.93.jar:/Users/asm/.m2/repository/log4j/log4j/1.2.16/log4j-1.2.16.jar:/Users/asm/.m2/repository/org/apache/commons/commons-io/1.3.2/commons-io-1.3.2.jar:/Users/asm/.m2/repository/org/apache/lucene/lucene-core/3.0.2/lucene-core-3.0.2.jar:/Users/asm/.m2/repository/org/codehaus/jackson/jackson-core-asl/1.6.2/jackson-core-asl-1.6.2.jar:/Users/asm/.m2/repository/org/codehaus/jackson/jackson-jaxrs/1.6.2/jackson-jaxrs-1.6.2.jar:/Users/asm/.m2/repository/org/codehaus/jackson/jackson-mapper-asl/1.6.2/jackson-mapper-asl-1.6.2.jar:/Users/asm/.m2/repository/org/cometd/java/bayeux-api/2.0.0/bayeux-api-2.0.0.jar:/Users/asm/.m2/repository/org/cometd/java/cometd-java-common/2.0.0/cometd-java-common-2.0.0.jar:/Users/asm/.m2/repository/org/cometd/java/cometd-java-server/2.0.0/cometd-java-server-2.0.0.jar:/Users/asm/.m2/repository/org/cometd/javascript/cometd-javascript-jquery/2.0.0/cometd-javascript-jquery-2.0.0.war:/Users/asm/.m2/repository/org/eclipse/jetty/jetty-continuation/7.1.5.v20100705/jetty-continuation-7.1.5.v20100705.jar:/Users/asm/.m2/repository/org/eclipse/jetty/jetty-util/7.1.5.v20100705/jetty-util-7.1.5.v20100705.jar:/Users/asm/.m2/repository/org/mortbay/jetty/jetty/6.1.25/jetty-6.1.25.jar:/Users/asm/.m2/repository/org/mortbay/jetty/jetty-util/6.1.25/jetty-util-6.1.25.jar:/Users/asm/.m2/repository/org/mortbay/jetty/servlet-api/3.0.20100224/servlet-api-3.0.20100224.jar:/Users/asm/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:/Users/asm/.m2/repository/org/slf4j/slf4j-simple/1.6.1/slf4j-simple-1.6.1.jar'
  };
*/

    var config = {
    contextPath: '${pageContext.request.contextPath}',
    sourcePath: '/Users/asm/Development/search/src/java:/Users/asm/Development/search/gen-java',
    classPath: '/Users/asm/Development/search/dist/etsy-search-0.1.0.jar:/Users/asm/Development/search/src/java:/Users/asm/Development/search/gen-java,:/Users/asm/Development/search/dist/*.jar:/Users/asm/Development/search/lib/default/jars/*:/Users/asm/Development/search/lib/thrift/*.jar:/Users/asm/Development/search/lib/*:/usr/share/ant/lib/ant.jar:/usr/share/ant/lib/ant-launcher.jar:/usr/java/jdk1.6.0_14/lib/tools.jar:/Users/asm/Development/ivy/java/dbunit/dbunit/2.4.8/jars/dbunit-2.4.8.jar:/Users/asm/Development/ivy/java/org.apache.solr/solr-core/4.0-dev-1021515/jars/solr-core-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/org.apache.solr/solr-solrj/4.0-dev-1021515/jars/solr-solrj-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/org.apache.solr/solr-commons-csv/4.0-dev-1021515/jars/solr-commons-csv-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/org.apache.solr/solr-dataimporthandler/4.0-dev-1021515/jars/solr-dataimporthandler-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/postgresql/postgresql/8.4-701.jdbc4/jars/postgresql-8.4-701.jdbc4.jar:/Users/asm/Development/ivy/java/org.apache.lucene/lucene-queries/4.0-dev-1021515/jars/lucene-queries-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/org.apache.lucene/lucene-core/4.0-dev-1021515/jars/lucene-core-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/org.apache.lucene/lucene-analyzers-common/4.0-dev-1021515/jars/lucene-analyzers-common-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/org.apache.lucene/lucene-spellchecker/4.0-dev-1021515/jars/lucene-spellchecker-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/org.apache.lucene/lucene-highlighter/4.0-dev-1021515/jars/lucene-highlighter-4.0-dev-1021515.jar:/Users/asm/Development/ivy/java/commons-fileupload/commons-fileupload/1.2.1/jars/commons-fileupload-1.2.1.jar:/Users/asm/Development/ivy/java/commons-cli/commons-cli/1.2/jars/commons-cli-1.2.jar:/Users/asm/Development/ivy/java/org.apache.commons/commons-io/1.4/jars/commons-io-1.4.jar:/Users/asm/Development/ivy/java/org.apache.thrift/thrift/0.5.0/jars/thrift-0.5.0.jar:/Users/asm/Development/ivy/java/joda-time/joda-time/1.6/jars/joda-time-1.6.jar:/Users/asm/Development/ivy/java/commons-lang/commons-lang/2.5/jars/commons-lang-2.5.jar:/Users/asm/Development/ivy/java/org.mongodb/mongo-java-driver/2.0rc4/jars/mongo-java-driver-2.0rc4.jar:/Users/asm/Development/ivy/java/org.apache.httpcomponents/httpclient/4.0.1/jars/httpclient-4.0.1.jar:/Users/asm/Development/ivy/java/org.mortbay.jetty/jetty/6.1.24/jars/jetty-6.1.24.jar:/Users/asm/Development/ivy/java/org.codehaus.jackson/jackson-core-asl/1.5.3/jars/jackson-core-asl-1.5.3.jar:/Users/asm/Development/ivy/java/org.codehaus.jackson/jackson-mapper-asl/1.5.3/jars/jackson-mapper-asl-1.5.3.jar:/Users/asm/Development/ivy/java/org.slf4j/jcl-over-slf4j/1.5.5/jars/jcl-over-slf4j-1.5.5.jar:/Users/asm/Development/ivy/java/org.slf4j/slf4j-api/1.5.5/jars/slf4j-api-1.5.5.jar:/Users/asm/Development/ivy/java/org.slf4j/slf4j-jdk14/1.5.5/jars/slf4j-jdk14-1.5.5.jar:/Users/asm/Development/ivy/java/net.sf.json-lib/json-lib/2.3/jars/json-lib-2.3.jar:/Users/asm/Development/ivy/java/org.yaml/snakeyaml/1.6/jars/snakeyaml-1.6.jar:/Users/asm/Development/ivy/java/google/guava/r05/jars/guava-r05.jar:/Users/asm/Development/ivy/java/tokyocabinet/tokyocabinet-java/1.23/jars/tokyocabinet-java-1.23.jar:/Users/asm/Development/ivy/java/mysql/mysql-connector-java/5.1.13/jars/mysql-connector-java-5.1.13.jar:/Users/asm/Development/ivy/java/commons-dbcp/commons-dbcp/1.4/jars/commons-dbcp-1.4.jar:/Users/asm/Development/ivy/java/fastutil/fastutil/5.1.5/jars/fastutil-5.1.5.jar:/Users/asm/Development/ivy/java/flexjson/flexjson/2.0/jars/flexjson-2.0.jar:/Users/asm/Development/ivy/java/junit/junit/4.8.1/jars/junit-4.8.1.jar:/Users/asm/Development/ivy/java/org.mockito/mockito/1.8.5/jars/mockito-1.8.5.jar:/Users/asm/Development/ivy/java/log4j/log4j/1.2.15/jars/log4j-1.2.15.jar:/Users/asm/Development/ivy/java/org.slf4j/slf4j-log4j12/1.5.5/jars/slf4j-log4j12-1.5.5.jar:/Users/asm/Development/ivy/java/org.slf4j/jcl-over-slf4j/1.5.5/jars/jcl-over-slf4j-1.5.5.jar:/Users/asm/Development/ivy/java/org.slf4j/jul-to-slf4j/1.5.5/jars/jul-to-slf4j-1.5.5.jar:/Users/asm/Development/ivy/java/org.slf4j/slf4j-api/1.5.5/jars/slf4j-api-1.5.5.jar:/Users/asm/Development/ivy/java/org.clojure/clojure/1.2.0/jars/clojure-1.2.0.jar:/Users/asm/Development/ivy/java/org.clojure/clojure-contrib/1.2.0/jars/clojure-contrib-1.2.0.jar:/Users/asm/Development/ivy/java/com.maxmind/geoip/1.2.4/jars/geoip-1.2.4.jar'
  };
  </script>
 <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/yui/2.8.1/build/reset-fonts-grids/reset-fonts-grids.css" />
 <link rel="stylesheet/less" href="css/style.less" type="text/css" />
 <script type="text/javascript" src="http://lesscss.googlecode.com/files/less-1.0.35.min.js"></script>
  <script type="text/javascript">
    less.env = "development";
    less.watch();
  </script>
</head>
<body>
  <div id="main"></div>
  <script type='text/javascript' 
          src='steal/steal.js?kopidoc,development'>   
  </script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json-2.2.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cometd.js"></script>
  <script type="text/javascript" src="connect.js"></script>
</body>
</html>
