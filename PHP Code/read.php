<?php
//$con = mysqli_connect("localhost", "root", "", "petfinder");
//if($con) {
//    $sql = "select * from images";
//    $result = mysqli_query($con, $sql);
//    while ($row = mysqli_fetch_assoc($result)){
//        echo "<div>";
//        echo "<img src='".$row['path']."' width='400px'>";
//        echo "<p>Email: ".$row['email']."</p>"; // echo email
//        echo "<p>Sector: ".$row['sector']."</p>"; // echo sector
//        echo "<p>Description: ".$row['description']."</p>"; // echo description
//        echo "</div>";
//    }
//}

header('Content-Type: application/json'); // Specify that we are returning JSON
$con = mysqli_connect("localhost", "root", "", "petfinder");
if ($con) {
    $stmt = $con->prepare("DELETE FROM images WHERE post_date < DATE_SUB(NOW(), INTERVAL 7 DAY)");
    if (!$stmt->execute()) {
        echo "Failed to delete old posts";
        exit();
    }
    $sql = "select * from images";
    $result = mysqli_query($con, $sql);
    $data = array(); // Initialize an array to store your data
    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = $row; // Push each row into the data array
    }
    echo json_encode($data); // Echo the JSON representation of the data array
}


