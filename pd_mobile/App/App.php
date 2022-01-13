<?php
class App
{
    static function bootstrap(){
        require_once 'Core/model.php';
        require_once 'Core/controller.php';
        require_once 'Core/route.php';
        Route::start(); // запускаем маршрутизатор
    }
    
    static function check_Logged(){
        if (isset($_SESSION['user'])) {
            return $_SESSION['user'];
        }
        return false;
    }

    static function init_DB()
    {
        $db = mysqli_connect('localhost','root','1488322den4ik','dictionary_db')
             or die("Ошибка: Невозможно подключиться к MySQL " . mysqli_error($db));
        return $db;
    }
}