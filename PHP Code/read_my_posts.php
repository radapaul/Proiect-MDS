<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$server = "localhost";
$username = "root";
$password = "";
$dbname = "petfinder";

$conn = new mysqli($server, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$email = isset($_GET['email']) ? $_GET['email'] : die();

$query = "SELECT * FROM images WHERE email = '$email'";

$result = $conn->query($query);
$outp = array();
$outp = $result->fetch_all(MYSQLI_ASSOC);

echo json_encode($outp);
?>
