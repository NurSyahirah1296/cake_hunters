<?php
error_reporting(0);
include_once ("dbconnect.php");
$email = $_POST['email'];
$password = sha1($_POST['password']);
$phone = $_POST['phone'];
$name = $_POST['name'];
$location = $_POST['location'];
$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];
$encoded_string = $_POST["encoded_string"];
$image_name = $_POST["image_name"];
$decoded_string = base64_decode($encoded_string);
$path = '../images/' . $image_name;
$file = fopen($path, 'wb');
$is_written = fwrite($file, $decoded_string);
fclose($file);
if ($is_written > 0) {
    $sqlinsert = "INSERT INTO USER(EMAIL,PASSWORD,PHONE,NAME,LOCATION,LATITUDE,LONGITUDE) VALUES ('$email','$password','$phone','$name','$location','$latitude','$longitude')";
    if ($conn->query($sqlinsert) === TRUE) {
        echo "success";
    } else {
        echo "failed";
    }
} else {
    echo "Upload Failed";
}
?>