package com.b4pay.order.controller;

import com.b4pay.order.entity.Result;
import com.b4pay.order.utils.DownloadUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @ClassName DownLoadController
 * @Description
 * @Version 2.1
 **/

@RestController
@RequestMapping("/download")
@CrossOrigin
public class DownLoadController {

    @RequestMapping(value = "/zip", method = RequestMethod.GET)
    public void download(HttpServletResponse response) {
        DownloadUtil downloadUtil = new DownloadUtil();
        File file = new File("");
        downloadUtil.prototypeDownload(file,"2.jpg",response,false);
    }
}
