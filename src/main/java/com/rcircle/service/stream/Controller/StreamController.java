package com.rcircle.service.stream.Controller;

import com.rcircle.service.stream.services.HLSService;
import com.rcircle.service.stream.services.MessageService;
import com.rcircle.service.stream.utils.HttpContext;
import com.rcircle.service.stream.utils.HttpContextHolder;
import com.rcircle.service.stream.utils.Toolkit;
import com.rcircle.service.stream.utils.core.CommandCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

@RestController
@RequestMapping("/stream")
public class StreamController {
    private Logger logger = LoggerFactory.getLogger(StreamController.class);
    private static final int TYPE_CREATE_HLS = 0;
    @Resource
    private HLSService hlsService;

    @Resource
    private MessageService messageService;

    private HlsProcessCallback hlsProcessCallback = new HlsProcessCallback();

    @PostMapping("/create")
    public String createHLSFiles(@RequestParam("src") String srcfile,
                                 @RequestParam("dst") String dstpath,
                                 @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                 @RequestParam(value = "id", required = false, defaultValue = "0") int logid,
                                 @RequestParam(value = "url", required = false, defaultValue = "") String baseurl) {
        MateData md = new MateData(type);
        md.token = HttpContextHolder.getContext().getValue(HttpContext.AUTH_TOKEN);
        switch (type) {
            case TYPE_CREATE_HLS:
                srcfile = Toolkit.decodeFromUrl(srcfile);
                dstpath = Toolkit.decodeFromUrl(dstpath);
                baseurl = Toolkit.decodeFromUrl(baseurl);
                md.filename = new File(srcfile).getName();
                md.srcfile = srcfile;
                md.id = logid;
                md.filepath = Toolkit.assembleAbsoluteFilePath(dstpath, "index.m3u8");
                hlsService.createHLSFiles(logid, srcfile, dstpath, md.filename, baseurl, md.filepath, md, hlsProcessCallback);
                break;
        }
        return "";
    }

    public class HlsProcessCallback implements CommandCallback<MateData> {

        @Override
        public void preProcess(MateData flag) {
            logger.info("(%d) start (src:%s, dst:%s)", flag.type, flag.srcfile, flag.filepath);
        }

        @Override
        public void processing(MateData flag, String ret) {
            logger.info("(%d) start (src:%s, dst:%s) | %s", flag.type, flag.srcfile, flag.filepath, ret);
        }

        @Override
        public void afterProcess(MateData flag) {
            boolean result = false;
            HttpContextHolder.getContext().setValue(HttpContext.AUTH_TOKEN, flag.token);
            switch (flag.type) {
                case TYPE_CREATE_HLS:
                    result = hlsService.checkHLSFilesCreatedResult(flag.filepath);
                    if (result) {
                        File file = new File(flag.srcfile);
                        file.delete();
                    }
                    while (!messageService.sendHLSSplitFinished(flag.id, flag.filename, result)) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    public class MateData {
        public int type;
        public String filename;
        public String url;
        public String srcfile;
        public int id;
        public String filepath;
        public String token;

        public MateData(int type) {
            this.type = type;
        }
    }
}
