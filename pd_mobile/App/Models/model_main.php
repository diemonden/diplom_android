<?php


class Model_Main extends Model {

    private function selectItemListQuery($table){
        $sql = 'SELECT id, title, parent_id, update_date FROM '.$table.' WHERE user_id='
            .$_SESSION['user']['id'];
        return mysqli_fetch_all(mysqli_query($this->db,$sql), MYSQLI_ASSOC);
    }

    public function addItemQuery($table, $name, $parent_id){

        $check_name_query = 'SELECT COUNT(id) FROM '.$table.' WHERE title ="'.$name.'" AND parent_id ='.$parent_id.'
                                AND user_id ='.$_SESSION['user']['id'];

        $check_result = mysqli_query($this->db,$check_name_query) or die (mysqli_error($this->db));

        if (mysqli_fetch_row($check_result)[0] == 1){
            return json_encode(array('success' => false, 'id' => null));
        }
        else{

            $add_query = 'INSERT INTO '.$table.' SET title ="'.$name
                .'", user_id = "'.$_SESSION['user']['id'].'", parent_id = "'.$parent_id.'",
                 update_date = "'.date(('Y/m/d H:i:s')).'"';

            return json_encode(array(
                'success' => mysqli_query($this->db, $add_query),
                'id' => mysqli_insert_id($this->db)));


        }
    }
    //ВКЛАДКИ
    public function selectBookmarks(){
        $query = 'SELECT id, content_id FROM bookmarks WHERE user_id ='.$_SESSION['user']['id'];
        $c_id = [];
        $bm_id = [];
        $res = mysqli_query($this->db,$query);
        while ($row = $res->fetch_assoc()) {
            array_push($c_id, $row['content_id']);
            array_push($bm_id, $row['id']);
        }
        $sql = 'SELECT id, title, parent_id FROM docs WHERE user_id='
            .$_SESSION['user']['id']. ' AND id IN(' . implode( ',', $c_id ). ' );';
        $res =mysqli_query($this->db,$sql);

        if($res !== false) {
            $res = mysqli_fetch_all($res, MYSQLI_ASSOC);
            $final = [];
            $i = 0;
            foreach ($res as $row) {
                $row['bm_id'] = $bm_id[$i];
                $i++;
                array_push($final, $row);
            }

            return $final;
        }
        return false;

    }


    public function save_bookmark($id){
        $query = 'INSERT INTO bookmarks SET user_id ='.$_SESSION['user']['id'].', content_id = '.$id;
	    mysqli_query($this->db, $query) or die(mysqli_error($this->db));
        return mysqli_insert_id($this->db);
    }
    public function delete_bookmark($id)
    {
        $query = 'DELETE FROM bookmarks WHERE id="'.$id.'"';
        return mysqli_query($this->db, $query) or die(mysqli_error($this->db));
    }

    public function delete($id, $table)
    {
        $query = 'DELETE FROM '.$table.' WHERE id="'.$id.'"';
        return mysqli_query($this->db, $query);
    }
    public function get_current_doc_content($id){
        $query = 'SELECT content FROM docs WHERE id='.$id;
        $res = mysqli_query($this->db,$query);
        return mysqli_fetch_assoc($res);
    }

	public function rename($id,$new_name, $table)
	{
		$query = 'UPDATE '.$table.' SET title = "'.$new_name.'", update_date="'.date(('Y/m/d H:i:s')).'" WHERE id='.$id;
		return mysqli_query($this->db,$query);
	}

	public function save($id,$upd_content)
	{
		$upd_content = mysqli_real_escape_string($this->db,$upd_content);
		$query = 'UPDATE docs SET 
                content =  "'.$upd_content.'", 
                update_date="'.date(('Y/m/d H:i:s')).'" 
                WHERE id = '.$id;
		return mysqli_query($this->db,$query);
	}
    public function get_data()
    {

        return array(
        	'user' => array('id' => $_SESSION['user']['id'], 'username' => $_SESSION['user']['username'], 'email' => $_SESSION['user']['email'],
                'registred_date' => $_SESSION['user']['registred_date'], 'status' => $_SESSION['user']['status']),
            'topics' => $this->selectItemListQuery('topics'),
            'docs' => $this->selectItemListQuery('docs'),
            'bookmarks' => $this->selectBookmarks());

    }

}