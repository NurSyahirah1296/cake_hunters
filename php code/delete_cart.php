<?php
error_reporting(0);
include_once("dbconnect.php");
$userid = $_POST['userid'];
$cakeid = $_POST['cakeid'];
    $sqldelete = "DELETE FROM CART WHERE USERID = '$userid' AND CAKEID='$cakeid'";
    if ($conn->query($sqldelete) === TRUE){
       echo "success";
    }else {
        echo "failed";
    }
?>