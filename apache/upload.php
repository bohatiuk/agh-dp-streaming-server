<?php

// Path to move uploaded files
$target_path = "C:\\Users\\julia\\dev\\DP\\SERVER\\storage\\tmp\\";

// array for final json respone
$response = array();

// getting server ip address
$server_ip = gethostbyname(gethostname());


if (isset($_FILES['image']['name'])) {
    $target_path = $target_path . basename($_FILES['image']['name']);    
    $response['file_name'] = basename($_FILES['image']['name']);


    try {
        // Throws exception incase file is not being moved
        if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
            // make error flag true
            $response['error'] = true;
            $response['message'] = 'Could not move the file!';
        }
		
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/dp_server_test_war_exploded/upload-finished?filename=".$response['file_name']);

    } catch (Exception $e) {
        // Exception occurred. Make error flag true
        $response['error'] = true;
        $response['message'] = $e->getMessage();
    }
} else {
    // File parameter is missing
    $response['error'] = true;
    $response['message'] = 'File parameter is missing';
}
// Echo final json response to client
echo json_encode($response);
?>