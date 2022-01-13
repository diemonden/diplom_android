<?php

use http\Client\Request;

class Controller_Registration extends Controller
{
	function __construct()
	{
		$this->model = new Model_Registration();
	}

    function action_check_email(){
        echo $this->model->check_email($_POST['email']);
	    //echo json_encode(array('success' => $this->model->check_email($_POST['email'])));
    }
	function action_signUp(){
		echo $this->model->add_user($_POST['email'],$_POST['name'],$_POST['pass']);
	}

}
