package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.entity.Asset;
import com.example.ProjectDima3.entity.Facility;
import com.example.ProjectDima3.entity.User;
import com.example.ProjectDima3.exception.ResourceNotFoundException;
import com.example.ProjectDima3.repository.AssetRepository;
import com.example.ProjectDima3.repository.FacilityRepository;
import com.example.ProjectDima3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<AssetDTO> getAllAssets(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Текущий пользователь не найден в базе данных"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        log.info("Получение страницы активов для пользователя: {}, Является админом: {}", username, isAdmin);

        Page<Asset> assetsPage;
        if (isAdmin) {
            assetsPage = assetRepository.findAll(pageable);
        } else {
            if (currentUser.getCompany() == null) {
                return Page.empty(pageable);
            }
            Long companyId = currentUser.getCompany().getId();
            assetsPage = assetRepository.findByCompanyId(companyId, pageable);
        }

        return assetsPage.map(AssetDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public AssetDTO getAssetById(Long id) {
        log.info("Получение актива по ID: {}", id);
        return assetRepository.findById(id)
                .map(AssetDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Актив не найден"));
    }

    @Transactional
    public AssetDTO createAsset(AssetDTO dto) {
        log.info("Создание нового актива: {}", dto.getName());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Текущий пользователь не найден в базе данных"));

        Asset asset = new Asset();
        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setStatus(dto.getStatus());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setDescription(dto.getDescription());

        asset.setUser(currentUser);
        asset.setCompany(currentUser.getCompany());
        asset.setDepartment(currentUser.getDepartment());

        if (dto.getFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getFacilityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Объект (Facility) с ID " + dto.getFacilityId() + " не найден"));
            asset.setFacility(facility);
        }

        Asset savedAsset = assetRepository.save(asset);
        return AssetDTO.fromEntity(savedAsset);
    }

    @Transactional
    public AssetDTO updateAsset(Long id, AssetDTO dto) {
        log.info("Обновление актива с ID: {}", id);
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Актив не найден"));
        
        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setStatus(dto.getStatus());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setDescription(dto.getDescription());

        if (dto.getFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getFacilityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Объект (Facility) с ID " + dto.getFacilityId() + " не найден"));
            asset.setFacility(facility);
        } else {
            asset.setFacility(null);
        }

        Asset updatedAsset = assetRepository.save(asset);
        return AssetDTO.fromEntity(updatedAsset);
    }

    @Transactional
    public void deleteAsset(Long id) {
        log.info("Попытка удаления актива с ID: {}", id);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Текущий пользователь не найден в базе данных"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdmin && !isManager) {
            log.warn("ОТКАЗ: Пользователь {} с ролью, отличной от ADMIN или MANAGER, пытался удалить актив.", username);
            throw new AccessDeniedException("У вас нет прав для удаления активов.");
        }

        Asset assetToDelete = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Актив с ID " + id + " не найден"));

        if (isAdmin) {
            log.info("Пользователь {} является админом. Удаление разрешено.", username);
            assetRepository.deleteById(id);
            return;
        }

        // Если пользователь менеджер, проверяем его права.
        Long managerCompanyId = (currentUser.getCompany() != null) ? currentUser.getCompany().getId() : null;
        Long assetCompanyId = (assetToDelete.getCompany() != null) ? assetToDelete.getCompany().getId() : null;

        if (managerCompanyId == null) {
            log.warn("ОТКАЗ: Менеджер {} без компании пытался удалить актив {}.", username, id);
            throw new AccessDeniedException("У вас нет компании, вы не можете удалять активы.");
        }

        if (!Objects.equals(managerCompanyId, assetCompanyId)) {
            log.warn("ОТКАЗ: Менеджер {} (компания {}) пытался удалить актив {} из другой компании ({}).",
                    username, managerCompanyId, id, assetCompanyId);
            throw new AccessDeniedException("Вы не можете удалять активы, не принадлежащие вашей компании.");
        }

        log.info("Пользователь {} (Менеджер компании {}) удаляет актив {}", username, managerCompanyId, id);
        assetRepository.deleteById(id);
    }
}
