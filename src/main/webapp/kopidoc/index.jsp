<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
  <title>KopiDoc</title>
  <script type="text/javascript">
    var config = {
    contextPath: '${pageContext.request.contextPath}',
    sourcePath: 'src/main/java',
    classPath: 'src/main/java'
    };
  </script>
 <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/yui/2.8.1/build/reset-fonts-grids/reset-fonts-grids.css" />
 <link rel="stylesheet/less" href="../css/style.less" type="text/css" />
 <script type="text/javascript" src="http://lesscss.googlecode.com/files/less-1.0.35.min.js"></script>
  <script type="text/javascript">
    less.env = "development";
    less.watch();
  </script>
</head>
<body>
  <div id="main"></div>
  <script type='text/javascript' 
          src='../steal/steal.js?kopidoc,development'>   
  </script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json-2.2.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cometd.js"></script>
  <script type="text/javascript" src="../connect.js"></script>
</body>
</html>
