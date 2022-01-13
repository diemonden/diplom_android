<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

session_start();
define('ROOT', 'http://localhost/pd_mobile/');
require_once 'App/App.php';
App::bootstrap();


