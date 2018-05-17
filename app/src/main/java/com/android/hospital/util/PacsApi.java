package com.android.hospital.util;

import android.content.Context;

/**
 * Created by WHL on 2015/2/7.
 */
public class PacsApi {
    
    public static final String PacsIP="192.168.2.101";
    
    
    
    
    //查询PACS服务器上的检查列表。
    //http://szxc.pacs120.com:88/Partner/Study.ashx?UserName=monovo&Password=monovo&PatientName=&PatientID=2012017521&StudyID=&StudyAccessionNumber=&StudyDateStart=&StudyDateEnd=&Modality=
/*    {
        "Status": "Scuess",
            "Page": "1",
            "Total": "1",
            "Records": "1",
            "Rows": [
        {
            "Name": "WuJiaHui",
                "Sex": "男",
                "Age": "30",
                "ID": "2012017521",
                "DOB": "1985-01-01",
                "StudyID": "20980",//病人检查ID
                "StudyDateTime": "2015-02-07 10:44:44",//检查日期
                "AccessionNumber": "84327",
                "StudyDescription": "",//检查描述
                "NumberOfSeries": "5",
                "NumberOfImages": "93",
                "Modality": "CT",
                "StudyUID": "1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180"
        }
        ],
        "OverMax": "false"
    }*/
    public static String getStudy(Context context,String patientId,String studyId){
        String requestUrl="http://"+PacsIP+
                "/Partner/Study.ashx?UserName=admin&Password=admin&PatientName=&PatientID="
                +patientId+"&StudyID=+"+studyId
                +"&StudyAccessionNumber=&StudyDateStart=&StudyDateEnd=&Modality=";
        return requestUrl;
    }
    
    
    
    
    
    //查询PACS服务器上的指定StudyUID下的Series列表。
    //http://szxc.pacs120.com:88/Partner/Series.ashx?UserName=monovo&Password=monovo&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&patientID=2012017521
/*    {
        "Status": "Scuess",
            "Records": "1",
            "StudyUID": "1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180",
            "SeriesList": [
        {
            "SeriesDescription": "//FL04",
                "SeriesUID": "1.2.392.200036.9116.2.5.1.16.1613469867.1423277122.471173",
                "SeriesNumber": "1",
                "NumberOfImages": "1"
        },
        {
            "SeriesDescription": "//FC23",
                "SeriesUID": "1.2.392.200036.9116.2.5.1.16.1613469867.1423277173.143346",
                "SeriesNumber": "2",
                "NumberOfImages": "24"
        },
        {
            "SeriesDescription": "//FC30",
                "SeriesUID": "1.2.392.200036.9116.2.5.1.16.1613469867.1423277226.18721",
                "SeriesNumber": "3",
                "NumberOfImages": "24"
        },
        {
            "SeriesDescription": "//FC23",
                "SeriesUID": "1.2.392.200036.9116.2.5.1.16.1613469867.1423277293.6895",
                "SeriesNumber": "4",
                "NumberOfImages": "22"
        },
        {
            "SeriesDescription": "//FC30",
                "SeriesUID": "1.2.392.200036.9116.2.5.1.16.1613469867.1423277341.600043",
                "SeriesNumber": "5",
                "NumberOfImages": "22"
        }
        ]
    }*/
    public static String getSeries(Context context,String patientId,String studyUid){
        String requestUrl="http://"+PacsIP+"/Partner/Series.ashx?UserName=admin&Password=admin&StudyUID="+studyUid+"&patientID="+patientId;
        return requestUrl;
    }
    
    
    
    
    
    //根据影像的StudyUID和SeriesUID返回本次检查的影像缩略图
    //http://szxc.pacs120.com:88/Partner/Thumbs.ashx?UserName=monovo&Password=monovo&Width=120&Height=100&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423277122.471173
    public static String getThumbs(String studyUid,String seriesUID){
        String requestUrl="http://"+PacsIP+"/Partner/Thumbs.ashx?UserName=admin&Password=admin&Width=120&Height=100&StudyUID="+studyUid+"&SeriesUID="+seriesUID;
        return requestUrl;
    }
    
    //影像序列图像数量
    //http://szxc.pacs120.com:88/Partner/Instance.ashx?UserName=monovo&Password=monovo&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423277122.471173
    //{"Status": "Scuess","Records": "1"}
    public static String getInstance(String studyUid,String seriesUID){
        String requestUrl="http://"+PacsIP+"/Partner/Instance.ashx?UserName=admin&Password=admin&StudyUID="+studyUid+"&SeriesUID="+seriesUID;
        return requestUrl;
    }

    //根据传递的参数显示图像。
    //http://szxc.pacs120.com:88/Partner/Image.ashx?UserName=monovo&Password=monovo&Width=1024&Height=768&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423277122.471173&Index=0&Layout=1x1&Invert=false&WW=&WC=&MoveX=&MoveY=&Angle=&FlipImage=&Mode=&ImageType=JPEG
    public static String getImage(String studyUid,String seriesUID,String index){
        String requestUrl="http://"+PacsIP+"/Partner/Image.ashx?UserName=admin&Password=admin&Width=1024&Height=768&StudyUID="+studyUid+"&SeriesUID="+seriesUID+"&Index="+index+"&Layout=1x1&Invert=false&WW=&WC=&MoveX=&MoveY=&Angle=&FlipImage=&Mode=&ImageType=JPEG";
        return requestUrl;
    }
    
}
