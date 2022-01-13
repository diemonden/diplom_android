<?php


class Model_Registration extends Model {


    function check_email($email){
        $sql = "SELECT COUNT(id) FROM `users` WHERE email='".$email."'";
        $result = mysqli_query($this->db,$sql);
        return  ( mysqli_fetch_row($result)[0]);

    }
    function add_user($email,$name,$pass){
        $sql = "INSERT INTO users SET 
            username = '".$name."', 
            password = '".$pass."', 
            email = '".$email."', 
            registred_date ='".date('Y/m/d H:i:s')."',
            status = '0'";
        return mysqli_query($this->db,$sql) or die("Ошибка " . mysqli_error($this->db));
    }
}