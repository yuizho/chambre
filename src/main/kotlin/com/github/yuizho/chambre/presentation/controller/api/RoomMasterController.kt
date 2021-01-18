package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.auth.dto.UserSession
import com.github.yuizho.chambre.application.service.room.RoomMasterService
import com.github.yuizho.chambre.domain.room.UnapprovedUser
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.exception.BusinessException
import com.github.yuizho.chambre.presentation.controller.api.dto.ApproveParamter
import com.github.yuizho.chambre.presentation.controller.api.dto.ApproveResponse
import com.github.yuizho.chambre.presentation.controller.api.dto.RejectParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.RejectResponse
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/api/room-master")
@RestController
class RoomMasterController(
        private val roomMasterService: RoomMasterService
) {
    @PostMapping("/approve")
    fun approve(@RequestBody @Valid param: ApproveParamter): Mono<ApproveResponse> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    Pair(
                            UnapprovedUser(User.Id(param.userId), param.userName),
                            (it.authentication.principal as UserSession).getTypedRoomId()
                    )
                }
                .switchIfEmpty(Mono.error(BusinessException("no session information")))
                .flatMap { (user, roomId) ->
                    roomMasterService.approve(user, roomId)
                }
                .then(Mono.just(ApproveResponse()))

    }

    @PostMapping("/reject")
    fun reject(@RequestBody @Valid param: RejectParameter): Mono<RejectResponse> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    (it.authentication.principal as UserSession).getTypedRoomId()
                }
                .switchIfEmpty(Mono.error(BusinessException("no session information")))
                .flatMap { roomId ->
                    roomMasterService.reject(User.Id(param.userId), roomId)
                }
                .then(Mono.just(RejectResponse()))
    }
}