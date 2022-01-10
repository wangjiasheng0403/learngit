package org.zznode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbParyMapper;
import org.zznode.entity.TbPary;
import org.zznode.service.ParyService;

@Service
public class ParyServiceImpl extends ServiceImpl<TbParyMapper, TbPary> implements ParyService {
}
