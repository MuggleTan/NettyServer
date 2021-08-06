package com.hunau.mapper;

import com.hunau.entity.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TanXY
 * @date 2021/4/24 - 14:46
 */
@Mapper
@Repository
public interface InfoMapper {

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
    List<Info> selectInfoList(@Param("info") Info info, @Param("page") int page, @Param("size") int size);

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
