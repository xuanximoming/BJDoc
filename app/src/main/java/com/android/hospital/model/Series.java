package com.android.hospital.model;

import java.util.List;

/**
 * Created by WHL on 2015/2/7.
 */
public class Series {
    
    public String SeriesDescription;
    public String SeriesUID;
    public String SeriesNumber;
    public String NumberOfImages;
    
    
    public class SeriesResult{
        public String Status;
        public String Records;
        public String StudyUID;
        public List<Series> SeriesList;
    }
    
}
