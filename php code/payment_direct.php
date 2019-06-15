<?php

$api_key = 'b47704d6-dd30-4143-a4b1-27eb88e8f906';
$host = 'https://billplz-staging.herokuapp.com/api/v3/bills';
$collection_id = 't4znxela';

$data = array(
          'collection_id' => $collection_id,
          'email' => 'customer@email.com',
          'mobile' => '60123456789',
          'name' => "Jone Doe",
          'amount' => 2000, // RM20
		  'description' => 'Test',
          'callback_url' => "http://yourwebsite.com/return_url",
          'redirect_url' => "http://google.com",
          'reference_1_label' => "Bank Code",
          'reference_1' => "TEST0021"
);

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

header("Location: {$bill['url']}?auto_submit=true");