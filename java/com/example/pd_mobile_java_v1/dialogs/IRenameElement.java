package com.example.pd_mobile_java_v1.dialogs;

public interface IRenameElement {
   void renameElement(String newTitle);
   default void cancelRename(){};
}
