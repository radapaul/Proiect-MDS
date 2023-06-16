<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {

//        if ($db->logIn("users", $_POST['username'], $_POST['password'])) {
//            echo "Login Success";

        $loginResult = $db->logIn("users", $_POST['username'], $_POST['password']);
        if ($loginResult !== false) {
            $result = array(
                'status' => 'success',
                'message' => 'Login Success',
                'fullname' => $loginResult['fullname'],
                'email' => $loginResult['email']
            );
            echo json_encode($result);


        } else {
            $result = array(
                'status' => 'error',
                'message' => 'Username or Password wrong'
            );
            echo json_encode($result);
        }
    } else {
        $result = array(
            'status' => 'error',
            'message' => 'Error: Database connection'
        );
        echo json_encode($result);
    }
} else {
    $result = array(
        'status' => 'error',
        'message' => 'All fields are required'
    );
    echo json_encode($result);
}
?>
