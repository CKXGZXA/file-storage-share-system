package top.ckxgzxa.filestoragesharesystem.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.common.utils.IPFSUtils;
import top.ckxgzxa.filestoragesharesystem.common.utils.JwtUtils;
import top.ckxgzxa.filestoragesharesystem.domain.po.FilePO;
import top.ckxgzxa.filestoragesharesystem.service.CMService;
import top.ckxgzxa.filestoragesharesystem.service.ChunkService;
import top.ckxgzxa.filestoragesharesystem.service.FileService;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 赵希奥
 * @date 2023/4/7 10:12
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 文件上传下载接口
 */

@RestController
@CrossOrigin
@RequestMapping("file")
public class FileController {

    private final Log log = LogFactory.get();

    @Resource
    private FileService fileService;

    @Resource
    private ChunkService chunkService;

    @Resource
    private CMService cmService;

    @Deprecated
    @Value("${file.path}")
    private String filePath;

    /**
     * 文件上传接口
     *
     * @param file  待上传文件
     * @param token 用户token
     * @return 上传结果
     */
    @PostMapping("/upload")
    private Result upload(@RequestParam("file") MultipartFile file,
                          @RequestHeader("Authorization") String token) {

        // 判断文件是否为空
        if (file.isEmpty()) {
            return Result.failed(HttpStatus.HTTP_BAD_REQUEST, "文件为空");
        }

        return fileService.upload(file, token);
    }

    /**
     * 文件下载接口
     *
     * @param fileId   文件id
     * @param token    用户token
     * @param response 响应
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileId") Long fileId,
                         @RequestHeader("Authorization") String token,
                         HttpServletResponse response) {
        Long userId = JwtUtils.getUserIdFromJwt(token);

        // 判断该用户是否具有访问该文件的权限
        try {
            if (!fileService.checkFileAccess(userId, fileId)) {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpStatus.HTTP_FORBIDDEN);
                Result<Object> result = Result.failed(HttpStatus.HTTP_FORBIDDEN, "无该文件访问权限");
                // response.setStatus(HttpStatus.HTTP_OK);
                response.getWriter().write(JSONUtil.toJsonStr(result));
            } else {
                // 从IPFS下载文件
                byte[] fileBytes;
                try {
                    fileBytes = fileService.downloadFromIPFS(userId, fileId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                ServletOutputStream out = null;

                out = response.getOutputStream();
                out.write(fileBytes);
                out.flush();
                out.close();
            }
        } catch (ChainMakerCryptoSuiteException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查文件是否已经上传或者上传了一部分, 作废
     * @param md5
     * @return
     * @deprecated
     */
    @Deprecated
    @GetMapping("/check")
    public Result checkFile(@RequestParam("md5") String md5) {
        log.info("检查md5: " + md5);

        // 首先检查是否有完整的文件
        Boolean isUploaded = fileService.selectFileByMd5(md5);
        Map<String, Object> data = new HashMap<>();
        data.put("isUploaded", isUploaded);
        if (isUploaded) {
            // 返回201，表示文件已存在
            return Result.success(HttpStatus.HTTP_CREATED, "文件已存在", data);
        }

        // 文件不存在, 查找分片信息, 返回给前端
        List<Integer> chunkList = chunkService.selectChunkByMd5(md5);
        data.put("chunkList", chunkList);

        return Result.success(HttpStatus.HTTP_CREATED, "", data);
    }

    /**
     * 获取文件列表
     *
     * @return
     */
    @Deprecated
    @GetMapping("/fileList")
    public Result getFileList() {
        List<FilePO> filePOList = fileService.selectFileList();
        return Result.success(HttpStatus.HTTP_OK, "文件列表查询成功", filePOList);
    }

    /**
     * 上传分片
     *
     * @param chunk      文件分片
     * @param md5        文件md5
     * @param index      当前分片下标
     * @param chunkTotal 分片总数
     * @param fileSize   文件大小
     * @param fileName
     * @param chunkSize
     * @return
     * @deprecated
     */
    @Deprecated
    @PostMapping("/upload/chunk")
    public Result uploadChunk(@RequestParam("chunk") MultipartFile chunk,
                              @RequestParam("md5") String md5,
                              @RequestParam("index") Integer index,
                              @RequestParam("chunkTotal") Integer chunkTotal,
                              @RequestParam("fileSize") Long fileSize,
                              @RequestParam("fileName") String fileName,
                              @RequestParam("chunkSize") Long chunkSize,
                              @RequestParam("userId") Long userId) throws IOException, ChainMakerCryptoSuiteException {
        String[] splits = fileName.split("\\.");
        String type = splits[splits.length - 1];
        String resultFileName = filePath + md5 + "." + type;

        chunkService.saveChunk(chunk, md5, index, chunkSize, resultFileName);
        log.info("上传分片：" + index + " ," + chunkTotal + "," + fileName + "," + resultFileName);
        if (Objects.equals(index, chunkTotal)) {

            // 等待5秒钟
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 构建AES密钥
            byte[] aesKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

            // 构建AES加密器
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey);

            // 准备将文件上传到IPFS, 并将文件信息存入数据库

            // 读取文件字节
            // String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            byte[] fileBytes = ResourceUtil.readBytes("static/files/" + md5 + "." + type);

            // 加密文件
            fileBytes = aes.encrypt(fileBytes);

            String cid = IPFSUtils.upload(fileBytes);


            FilePO filePO = new FilePO(fileName, md5, fileSize);
            filePO.setUuid(UUID.fastUUID().toString());
            filePO.setCid(cid);
            filePO.setStatus(0);
            fileService.addFile(filePO);

            // todo 准备上链工作, 将文件信息和解密信息存入区块链
            String txId = cmService.saveFileInfo(userId, filePO, aesKey);

            chunkService.deleteChunkByMd5(md5);

            return Result.success(HttpStatus.HTTP_OK, "文件上传成功"/* , new HashMap<String, String>(2){
                {
                    put("cid", cid);
                    put("txId", txId);
                }
            } */);
        } else {
            return Result.success(HttpStatus.HTTP_CREATED, "分片上传成功", index);
        }
    }

    /**
     * 分片下载
     *
     * @param md5
     * @param fileName
     * @param chunkSize
     * @param chunkTotal
     * @param index
     * @param response
     * @deprecated
     */
    @Deprecated
    @PostMapping("/download")
    public void download(@RequestParam("md5") String md5,
                         @RequestParam("fileName") String fileName,
                         @RequestParam("chunkSize") Integer chunkSize,
                         @RequestParam("chunkTotal") Integer chunkTotal,
                         @RequestParam("index") Integer index,
                         HttpServletResponse response) {
        String[] splits = fileName.split("\\.");
        String type = splits[splits.length - 1];
        String resultFileName = filePath + md5 + "." + type;

        File resultFile = new File(resultFileName);

        long offset = (long) chunkSize * (index - 1);
        if (Objects.equals(index, chunkTotal)) {
            offset = resultFile.length() - chunkSize;
        }
        byte[] chunk = chunkService.getChunk(index, chunkSize, resultFileName, offset);


        log.info("下载文件分片" + resultFileName + "," + index + "," + chunkSize + "," + chunk.length + "," + offset);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + (chunk.length));


        response.setContentType("application/octet-stream");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(chunk);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存文件信息
     *
     * @param file
     * @return
     * @deprecated
     */
    @Deprecated
    @PostMapping("/save")
    @ResponseBody
    public Result save(@RequestBody FilePO file) {
        return fileService.saveFile(file);
    }

    /**
     * 根据cid获取文件下载url
     *
     * @param cid
     * @return
     * @deprecated
     */
    @Deprecated
    @GetMapping("/{cid}")
    public Result getUrlFromCid(@PathVariable("cid") String cid) {
        return fileService.getUrlFromCid(cid);
    }

}
