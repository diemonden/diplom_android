<?php

class Controller_Main extends Controller
{
	function __construct()
	{
		$this->model = new Model_Main();
	}
	
    function action_add()
    {
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $title = $jsonarray["title"];
        $parent_id = $jsonarray["parent_id"];
        $table = $jsonarray["table"];
        echo $this->model->addItemQuery($table, $title,$parent_id);
    }

    function action_delete()
    {
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $id = $jsonarray["id"];
        $table = $jsonarray["table"];
        echo json_encode(array('res' =>$this->model->delete($id,$table)));
    }
    //СОХРАНЕНИЕ ОТКРЫТОЙ ВКЛАДКИ
    function action_save_bookmark()
    {
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $doc_id = $jsonarray["id"];
        echo json_encode(array('res' =>$this->model->save_bookmark($doc_id)));
    }
    //ЗАКРЫТИЕ ОТКРЫТОЙ ВКЛАДКИ
    function action_delete_bookmark()
    {
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $bm_id = $jsonarray["id"];
        echo json_encode(array('res' =>$this->model->delete_bookmark($bm_id)));
    }
    //ПОЛУЧЕНИЕ ДАННЫХ
    function action_getData()
    {
        echo json_encode($this->model->get_data());
    }
    function action_chose_doc()
    {
        echo file_get_contents('http://localhost/pd_mobile/doc/generatePage/'.$_POST['id']);
    }
	function action_get_cur_doc()
	{
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $id = $jsonarray["id"];
		echo json_encode($this->model->get_current_doc_content($id));
	}

    function action_rename(){
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $id = $jsonarray["id"];
        $new_name = $jsonarray["new_name"];
        $table = $jsonarray["table"];
        echo  json_encode(array('res' =>$this->model->rename($id,$new_name,$table)));
    }
	function action_save(){
        $jsonarray = json_decode(file_get_contents('php://input'),true);
        $id = $jsonarray["id"];
        $upd_content = $jsonarray["upd_content"];
		echo  json_encode(array('res' =>$this->model->save($id,$upd_content)));
	}
    //ЛОГАУТ
    function action_logout()
    {
        unset($_SESSION['user']);
    }
}