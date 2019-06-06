package org.multimodule.common.repository;

import java.util.Optional;

import org.multimodule.common.entity.UserDevice;
import org.multimodule.common.entity.token.RefreshToken;
//import com.accolite.pru.health.AuthApp.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    @Override
    Optional<UserDevice> findById(Long id);

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findByUserId(Long userId);
}
