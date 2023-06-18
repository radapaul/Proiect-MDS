<?php
// Database connection
$con = mysqli_connect("localhost", "root", "", "petfinder");


$image_path = $_POST["image_path"];
$stmt = $con->prepare("UPDATE images SET post_date = CURRENT_TIMESTAMP WHERE path = ?");
$stmt->bind_param("s", $image_path);
if ($stmt->execute()) {
    echo "Success";
} else {
    echo "Failed to update post_date";
}

$stmt->close();
$con->close();
?>
