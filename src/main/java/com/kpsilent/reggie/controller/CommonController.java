package com.kpsilent.reggie.controller;

import com.kpsilent.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        // 使用UUID随机生成文件id
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + suffix;

        // 如果basePath不存在则创建对应目录
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    /**
     * 下载图片
     * @param filename
     * @param response
     */
    @GetMapping("/download")
    public void download(@RequestParam("name") String filename, HttpServletResponse response){
        // 构造输入输出流来读取和输出文件
        FileInputStream fis = null;
        ServletOutputStream fos = null;
        try {
            // 输入流
            fis = new FileInputStream(new File(basePath + filename));
            // 输出流
            fos = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fis.read(bytes)) != -1){
                fos.write(bytes, 0, len);
                fos.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭输入输出流

            try {
                if(fos != null) fos.close();
                if(fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
