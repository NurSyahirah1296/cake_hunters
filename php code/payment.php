<?php
error_reporting(0);
include_once("dbconnect.php");

$userid = $_GET['userid']; 
$mobile = $_GET['mobile']; 
$name = $_GET['name']; 
$amount = $_GET['amount']; 
$orderid = $_GET['orderid'];
//$desc = $_POST['desc']; 
//$orderid = generateRandomString().'-'.$_GET['orderid']; 

$api_key = 'b47704d6-dd30-4143-a4b1-27eb88e8f906';
$host = 'https://billplz-staging.herokuapp.com/api/v3/bills';
$collection_id = 't4znxela';

$data = array(
          'collection_id' => $collection_id,
          'email' => $userid,
          'mobile' => $mobile,
          'name' => $name,
          'amount' => $amount * 100, // RM20
		  'description' => 'Payment for order id '.$orderid,
          'callback_url' => "http://yourwebsite.com/return_url",
          'redirect_url' => "http://uumresearch.com/foodninja/php/payment_update.php?userid=$userid&mobile=$mobile&amount=$amount&orderid=$orderid" 
);



//https://www.google.com/?billplz[id]=pbbmbsiv&billplz[paid]=true&billplz[paid_at]=2019-03-27%2006%3A37%3A36%20%2B0800&billplz[x_signature]=916349e1d4be53130a64b9221cf58bafa543ad6638592eae7ec347696bb010f5

$process = curl_init($host );
curl_setopt($process, CURLOPT_HEADER, 0);
curl_setopt($process, CURLOPT_USERPWD, $api_key . ":");
curl_setopt($process, CURLOPT_TIMEOUT, 30);
curl_setopt($process, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($process, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($process, CURLOPT_SSL_VERIFYPEER, 0);
curl_setopt($process, CURLOPT_POSTFIELDS, http_build_query($data) ); 

$return = curl_exec($process);
curl_close($process);

$bill = json_decode($return, true);

//echo "<pre>".print_r($bill, true)."</pre>";
header("Location: {$bill['url']}");

function generateRandomString($length = 5) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}
?>