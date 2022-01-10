package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.zznode.entity.TbModelPictureAppendix;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_model_picture_appendix】的数据库操作Mapper
 * @Entity generator.domain.TbModelPictureAppendix
 */
public interface TbModelPictureAppendixMapper extends BaseMapper<TbModelPictureAppendix> {
    /**
     * 场景图片元素
     *
     * @param dataId    dataId
     * @param pictureId pictureId
     * @return list
     */
    List<Map<String, Object>> getPictureAppendixByDataIdAndPictureId(@Param(value = "dataId") String dataId, @Param(value = "pictureId") int pictureId);
}




