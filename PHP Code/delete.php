<?php
// Database connection
$con = mysqli_connect("localhost", "root", "", "petfinder");

$image_path = $_POST["image_path"];
$stmt = $con->prepare("DELETE FROM images WHERE path = ?");
$stmt->bind_param("s", $image_path);

if ($stmt->execute()) {
    echo "Success";
} else {
    echo "Failed to delete post";
}

$stmt->close();
$con->close();
?>
