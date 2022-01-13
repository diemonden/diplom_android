package com.example.pd_mobile_java_v1.model;

import com.example.pd_mobile_java_v1.util.ISelectable;

import java.util.Date;

public interface IListItem extends ISelectable {
   int getId();
   String getTitle();
   void setTitle(String title);
   Date getUpdate_date();
}
