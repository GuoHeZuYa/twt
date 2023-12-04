package cn.twt.open.utils;

import cn.twt.open.exception.FileEmptyException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class FileUploadUtils {
    private static final String LOCAL_STORAGE = "/home/data/twtopen/pic/";

    private static final String URL = "/twtopen/pic/";

    private static final List<String> PIC_SUFFIX = Lists.newArrayList(".jpg",".jpeg",".png",".bmp",".gif");

    private static final String SEPARATOR = ".";

    public static String uploadFile(MultipartFile file){
        if (Objects.isNull(file)){
            throw new FileEmptyException();
        }
        try {
            String filename = file.getOriginalFilename();
            if (StringUtils.isBlank(filename) || !filename.contains(SEPARATOR)){
                return null;
            }
            String suffix = filename.substring(filename.lastIndexOf(".")).toLowerCase();
            if (!PIC_SUFFIX.contains(suffix)) {
                return null;
            }
            String newFilename = UUID.randomUUID()+suffix;
            File targetFile = new File(LOCAL_STORAGE+newFilename);

            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            try {
                file.transferTo(targetFile);
                return (URL+newFilename);
            } catch (IOException e){
                log.error("Exception",e);
                return null;
            }
        } catch (Exception e) {
            log.error("Exception",e);
            return null;
        }

    }
}
