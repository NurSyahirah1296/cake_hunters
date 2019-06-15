<?php
error_reporting(0);
include_once("dbconnect.php");
$cakeid = $_POST['cakeid'];
$userid = $_POST['userid'];
$quantity = $_POST['quantity'];
$price = $_POST['foodprice'];
$cakename = $_POST['cakename'];
$bakeryid = $_POST['bakeryid'];
$status = "not paid";
    
$sqlsel = "SELECT * FROM CAKES WHERE CAKEID = '$cakeid'";
$result = $conn->query($sqlsel);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $qavail = $row["QUANTITY"];
    }
    $bal = $qavail - $quantity; 
}
if ($bal>0){
    $sqlgetid = "SELECT * FROM CART WHERE USERID = '$userid' AND STATUS='not paid'";
    $result = $conn->query($sqlgetid);
    $sqlupdate = "UPDATE CAKES QUANTITY = '$bal' WHERE CAKEID = '$cakeid'";
        $conn->query($sqlupdate);
        
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $orderid = $row["ORDERID"];
    }
     $sqlinsert = "INSERT INTO CART(CAKEID,USERID,QUANTITY,PRICE,CAKENAME,STATUS,BAKERYID,ORDERID) VALUES ('$cakeid','$userid','$quantity','$price','$cakename','$status','$bakeryid','$orderid')";
     
    if ($conn->query($sqlinsert) === TRUE){
       echo "success";
    }
}else{
    $orderid = generateRandomString();
   $sqlinsert = "INSERT INTO CART(CAKEID,USERID,QUANTITY,PRICE,CAKENAME,STATUS,BAKERYD,ORDERID) VALUES ('$cakeid','$userid','$quantity','$price','$cakename','$status','$bakeryid','$orderid')";
    if ($conn->query($sqlinsert) === TRUE){
       echo "success";
    }
}
}



function generateRandomString($length = 7) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return date('dmY')."-".$randomString;
}

?>