package com.khoa.entity;

import java.util.ArrayList;
import java.util.List;

public class MultifileRespone {
    
    private List<FileRespone> multifileRespone = new ArrayList<FileRespone>();
    
    public void addRespone(FileRespone fileRespone){
        multifileRespone.add(fileRespone);
    }
}
