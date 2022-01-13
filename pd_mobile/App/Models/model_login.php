<?php


class Model_Login extends  Model {

	function getUsers() {
		$sql = "SELECT * FROM `users`";
		$result = mysqli_query($this->db,$sql) or die (mysqli_error($this->db));
		return mysqli_fetch_all(mysqli_query($this->db,$sql), MYSQLI_ASSOC);
	}
    function authorisation($email, $pass) {
        $sql = "SELECT * FROM `users` WHERE email='".$email."' AND password='".$pass."'";
        $result = mysqli_query($this->db,$sql);

        if(mysqli_num_rows($result) == 1) {
            $_SESSION['user'] = mysqli_fetch_assoc($result);
            return true;
        }
            return false;

    }
}