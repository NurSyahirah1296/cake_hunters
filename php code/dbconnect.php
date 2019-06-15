<?php
$servername = "localhost";
$username   = "id9631577_tiamobakery";
$password   = "tiamobakery19";
$dbname     = "id9631577_tiamobakery";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>