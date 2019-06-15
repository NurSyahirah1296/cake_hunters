<?php
error_reporting(0);
include_once("dbconnect.php");
$cakeid = $_POST['cakeid'];
$userid = $_POST['userid'];
$quantity = $_POST['quantity'];
$price = $_POST['price'];
$cakename = $_POST['cakename'];
$status = "not complete";

$sqlsel = "SELECT * FROM CAKES WHERE CAKEID = '$cakeid'";
$result = $conn->query($sqlsel);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $qavail = $row["QUANTITY"];
    }
    $bal = $qavail - $quantity; 
}

$sqlsel = "SELECT * FROM CAKES WHERE CAKEID = '$cakeid'";
$result = $conn->query($sqlsel);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $qavail = $row["QUANTITY"];
    }
    $bal = $qavail - $quantity; 
    if ($bal>0){
        $sqlupdate = "UPDATE CAKES QUANTITY = '$bal' WHERE CAKEID = '$cakeid'";
        $conn->query($sqlupdate);
        $sqlinsert = "INSERT INTO CART(CAKEID,USERID,QUANTITY,PRICE,CAKENAME,STATUS) VALUES ('$cakeid','$userid','$quantity','$price','$cakename','$status')";
        if ($conn->query($sqlinsert) === TRUE){
            echo $bal."success";
        }else {
            echo "failed";
        }
    }
}else{
    echo "failed";
}



?>