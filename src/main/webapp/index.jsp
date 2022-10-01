<%@ page language="java" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Hello World!</title>
	<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <h1>中文測試</h1>
    
    <form action="postData" method="POST"    accept-charset="utf-8"  >
      <input name="att01"  id="att01" type="text" value="中文" />
      <input type="submit" value="commit" />
    </form>
</body>
</html>