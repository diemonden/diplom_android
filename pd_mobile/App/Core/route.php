<?php
	class Route
	{
	static function start()
	{
	// контроллер и действие по умолчанию
		$controller_name = 'main';
		$action_name = 'index';
		$routes = explode('/', $_SERVER['REQUEST_URI']);

		// получаем имя контроллера
		if ( !empty($routes[2]) )
		{
			$controller_name = $routes[2];
		}

		// получаем имя экшена
		if ( !empty($routes[3]) )
		{
			$action_name = $routes[3];
		}
        if ( !empty($routes[4]) )
        {
            $param = $routes[4];
        }
		// добавляем префиксы
		$model_name = 'model_'.$controller_name;
		$controller_name = 'controller_'.$controller_name;
		$action_name = 'action_'.$action_name;

		// подцепляем файл с классом модели (файла модели может и не быть)

		$model_file = strtolower($model_name).'.php';
		$model_path = "App/Models/".$model_file;
		if(file_exists($model_path))
		{
			include "App/Models/".$model_file;
		}

		// подцепляем файл с классом контроллера
		$controller_file = strtolower($controller_name).'.php';
		$controller_path = "App/Controllers/".$controller_file;
		if(file_exists($controller_path))
		{
			include "App/Controllers/".$controller_file;
		}

		// создаем контроллер
        $controller = new $controller_name;
        $action = $action_name;
        //если контроллер ДОК и метод !екзистс
        //экшн = айди и вызов метода чуз_док
        if (method_exists($controller, $action)) {
            // вызываем действие контроллера

            if ( isset($param))
                $controller->$action($param);
            else
                $controller->$action();
        }
        }

		function ErrorPage404()
		{
			$host = 'http://'.$_SERVER['HTTP_HOST'].'/';
			header('HTTP/1.1 404 Not Found');
			header("Status: 404 Not Found");
			header('Location:'.$host.'404');
		}
	}
