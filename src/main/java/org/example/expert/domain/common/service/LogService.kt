package org.example.expert.domain.common.service

import org.example.expert.domain.common.entity.Log
import org.example.expert.domain.common.repository.LogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class LogService (
    private val logRepository: LogRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveLog(entity: String, action: String) {
        val log = Log(entity, action)
        logRepository.save(log)
    }
}
