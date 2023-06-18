<?php
$con = mysqli_connect("localhost", "root", "", "petfinder");
if($con) {
    if (!empty($_POST['image'])) {
        $path = 'images/' . date("d-m-Y") . '-' . time() . '-' . rand(10000, 100000) . '.jpg';
        if (file_put_contents($path, base64_decode($_POST['image']))) {
            $sector = $_POST['sector'];
            $description = $_POST['description'];
            $email = $_POST['email'];
            $sql = "INSERT INTO images (path, sector, description, email, post_date) VALUES ('".$path."', '".$sector."', '".$description."', '".$email."', NOW())";
            if(mysqli_query($con, $sql)){
                echo 'success';
            }
            else echo 'Failed to insert to Database';
        } else echo 'Failed to upload image';
    } else echo 'No image found';
}
else echo "Database connection failed";
