package com.rcircle.service.stream.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.stream.clients.RemoteMessageFeignClient;
import com.rcircle.service.stream.utils.Toolkit;
import com.rcircle.service.stream.utils.core.CommandCallback;
import com.rcircle.service.stream.utils.core.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class HLSService{
    @Autowired
    private CommandExecutor commandExecutor;

    @Value("${ffmpeg.bin.path}")
    private String ffmpeg_bin_path;

    public<T> void createHLSFiles(int id, String srcfile, String dstpath, String filename, String baseurl, String indexpath, T object, CommandCallback<T> callback) {
        File dstFile = new File(dstpath);
        if (!dstFile.exists()) {
            dstFile.mkdirs();
        }
        List<String> cmd = new ArrayList<>();
        cmd.add(Toolkit.assembleAbsoluteFilePath(ffmpeg_bin_path, "ffmpeg"));
        cmd.add("-i");
        cmd.add(srcfile);
        cmd.add("-c:a");
        cmd.add("aac");
        cmd.add("-f");
        cmd.add("hls");
        if (baseurl != null) {
            cmd.add("-hls_base_url");
            cmd.add(Toolkit.assembleBaseUrl(baseurl, id, filename));
        }
        cmd.add("-bsf:v");
        cmd.add("h264_mp4toannexb");
        cmd.add("-hls_list_size");
        cmd.add("0");
        // cmd.add("-vf");
        // cmd.add("scale=1280:-1");
        cmd.add("-r");
        cmd.add("25");
        cmd.add(indexpath);
        commandExecutor.setCmd(cmd).asyncProcess(object, callback);
    }

    public boolean checkHLSFilesCreatedResult(String filepath) {
        File indexFile = new File(filepath);
        return indexFile.exists();
    }



}
