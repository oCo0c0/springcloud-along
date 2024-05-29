package com.along.common.dao;

import com.along.common.entity.TxLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/5/29
 */
@Repository
public interface TxLogDao extends JpaRepository<TxLog, Long> {
}
