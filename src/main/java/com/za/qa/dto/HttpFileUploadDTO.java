package com.za.qa.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 类HttpFileUploadDTO.java的实现描述：http文件上传参数DTO
 * 
 * @author linyun 2017年2月7日 上午11:36:39
 */
public class HttpFileUploadDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4047177030550739325L;

    /**
     * 文件上传列表
     */
    private List<String>      fileList;

    /**
     * MultipartFile名称 例如：file、imageFile等
     */
    private String            pkgName;

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

}
