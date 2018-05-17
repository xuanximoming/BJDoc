package com.android.hospital.model;

import java.util.List;

/**
 * Created by WHL on 2015/2/7.
 */
public class Study {
    
    public String Name;
    public String Sex;
    public String Age;
    public String ID;
    public String DOB;
    public String StudyID;
    public String StudyDateTime;
    public String AccessionNumber;
    public String StudyDescription;
    public String NumberOfSeries;
    public String NumberOfImages;
    public String Modality;
    public String StudyUID;
    
    
    public class StudyResult{
        public String Status;
        public String Page;
        public String Total;
        public String Records;
        public String OverMax;
        public List<Study> Rows;
    }
    
}
