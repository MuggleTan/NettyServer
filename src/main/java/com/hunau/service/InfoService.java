package com.hunau.service;

import com.hunau.entity.Info;

import java.util.List;

/**
 * @author TanXY
 * @date 2021/4/24 - 16:35
 */
public interface InfoService {

    /**
     * 分页查询数据
     *
     * @param page 页码
     * @param size 每页数据量
     * @param info 查询信息
     * @return java.util.List<com.hunau.entity.Info>
     * @author TanXY
     * @date 2021/4/24 16:32
     */
    List<Info> selectInfoList(Info info,int page, int size);

    /**
     * 查询总数据量，用于分页显示
     *
     * @return int
     * @author TanXY
     * @date 2021/4/24 16:33
     */
    int countInfo();

    /**
     * 添加信息
     *
     * @param info 添加的信息
     * @return int
     * @author TanXY
     * @date 2021/4/24 16:34
     */
    int addInfo(Info info);
}
