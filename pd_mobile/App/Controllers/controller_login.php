<?php

class Controller_Login extends Controller
{
	function __construct()
	{
		$this->model = new Model_Login();
	}

	function action_getUsers()
	{
		echo json_encode($this->model->getUsers());
	}

    function action_signIn()
    {
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $email = $jsonarray["email"];
        $pass = $jsonarray["pass"];
        echo json_encode(array("res" => $this->model->authorisation($email,$pass),));
    }

    function action_checkLogged()
    {
        echo json_encode(App::check_Logged());
    }
}