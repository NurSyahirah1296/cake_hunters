<?php
error_reporting(0);
include_once("dbconnect.php");
$userid = $_POST['userid'];
$sql = "SELECT * FROM USER WHERE PHONE = '$userid'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["user"] = array();
    while ($row = $result ->fetch_assoc()){
        $userarray = array();
        $userarray[email] = $row["EMAIL"];
        $userarray[phone] = $row["PHONE"];
        $userarray[name] = $row["NAME"];
        $userarray[location] = $row["LOCATION"];
        $userarray[latitude] = $row["LATITUDE"];
        $userarray[longitude] = $row["LONGITUDE"];
         array_push($response["user"], $userarray);
    }
    echo json_encode($response);
}

?>