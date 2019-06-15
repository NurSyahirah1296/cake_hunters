<?php
$email = $_GET["email"];
$key = $_GET["key"];
if (strlen($key)<1){
    header("Location: /cakehunters/fault.html");
    exit;
}

?>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <style>
input[type=password], select {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

input[type=submit] {
  width: 100%;
  background-color: #4CAF50;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

input[type=submit]:hover {
  background-color: #45a049;
}

div {
  border-radius: 5px;
  background-color: #f2f2f2;
  padding: 20px;
}
</style>
</head>
<title>Reset Password </title>
<h2>Food Ninja </h2><br>
<p>Reset Password for <? echo $email ?></p>

<body>
    <br>
    <form action="/cakehunters/resetpassfinal.php" method="post" id="nameform">
  New Password: <br><input type="password" name="newpass" required><br>
  <input type="hidden" name="key" value ="<? echo $key ?>">
  <input type="hidden" name="email" value ="<? echo $email ?>">
   <input type="submit" value="Submit">
</form>
</html>