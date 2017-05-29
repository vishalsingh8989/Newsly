<?php
/* Attempt MySQL server connection. Assuming you are running MySQL
server with default setting (user 'root' with no password) */
$link = mysqli_connect("localhost", "root", "", "android");

// Check connection
if($link === false){
    die("ERROR: Could not connect. " . mysqli_connect_error());
}


$addtime=$_GET['addtime'];
// Attempt select query execution
if($addtime != null || $addtime != ""){
	$sql = "SELECT * FROM newsly where addtime > ". $addtime. " ORDER BY addtime DESC limit 30";
}else{
	$sql = "SELECT * FROM newsly  ORDER BY addtime DESC limit 30";
}

$result = $result = mysqli_query($link, $sql);
      // check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["articles"] = array();

    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["id"] = $row["id"];
        $product["description"] = $row["description"];
        $product["title"] = $row["title"];
        $product["publishedat"] = $row["publishedat"];
        $product["author"] = $row["author"];
        $product["sourceName"] = $row["sourceName"];
        $product["urltoimage"] = $row["urltoimage"];

        // push single product into final response array
        array_push($response["articles"], $product);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No products found";

    // echo no users JSON

}

// Close connection
mysqli_close($link);
?>

