<?php
class Model
{
    public $db;
    function __construct()
    {
        $this->db = App::init_DB();
    }
}
