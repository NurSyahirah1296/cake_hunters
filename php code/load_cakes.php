<?php
error_reporting(0);
include_once("dbconnect.php");
$restid = $_POST['restid'];

$sql = "SELECT * FROM CAKES WHERE BAKERYID = '$bakeryid'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["food"] = array();
    while ($row = $result ->fetch_assoc()){
        $cakelist = array();
        $cakelist[cakeid] = $row["CAKEID"];
        $cakelist[cakename] = $row["CAKENAME"];
        $cakelist[cakeprice] = $row["CAKEPRICE"];
        $cakelist[quantity] = $row["QUANTITY"];
        array_push($response["cake"], $cakelist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>