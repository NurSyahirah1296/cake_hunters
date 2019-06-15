<?php
error_reporting(0);
include_once("dbconnect.php");
$phone = $_POST['userid'];

$sql = "SELECT * FROM ORDERED WHERE USERID = '$phone'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["history"] = array();
    while ($row = $result ->fetch_assoc()){
        $historylist = array();
        $historylist[orderid] = $row["ORDERID"];
        $historylist[total] = $row["TOTAL"];
        $historylist[date] = $row["DATE"];
        array_push($response["history"], $historylist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>