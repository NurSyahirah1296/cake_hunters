<?php
error_reporting(0);
include_once("dbconnect.php");
$location = $_POST['location'];
if (strcasecmp($location, "All") == 0){
    $sql = "SELECT * FROM BAKERY"; 
}else{
    $sql = "SELECT * FROM BAKERY WHERE LOCATION = '$location'";
}
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["bakery"] = array();
    while ($row = $result ->fetch_assoc()){
        $bakerylist = array();
        $bakerylist[bakeryid] = $row["BAKERYID"];
        $bakerylist[name] = $row["NAME"];
        $bakerylist[phone] = $row["PHONE"];
        $bakerylist[address] = $row["ADDRESS"];
        $bakerylist[location] = $row["LOCATION"];
        array_push($response["bakery"], $bakerylist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>