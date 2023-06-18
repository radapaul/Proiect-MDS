<?php

$con = mysqli_connect("localhost", "root", "", "petfinder");

$image_path = $_POST['image_path'];
$description = $_POST['description'];

$query = "UPDATE images SET description=? WHERE path=?";

if($stmt = $con->prepare($query)){
    $stmt->bind_param("ss", $description, $image_path);
    $stmt->execute();
    if ($stmt->affected_rows > 0) {
        echo "Success";
    } else {
        echo "Failed";
    }
    $stmt->close();
} else {
    echo "Failed";
}

$con->close();

?>
