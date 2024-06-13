package site.keydeuk.store.domain.shipping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.shipping.dto.request.SaveShippingAddressRequest;
import site.keydeuk.store.domain.shipping.dto.response.ShippingAddressResponse;
import site.keydeuk.store.domain.shipping.repository.ShippingRepository;
import site.keydeuk.store.entity.ShippingAddress;
import site.keydeuk.store.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingService {
    private final ShippingRepository shippingRepository;

    @Transactional
    public ShippingAddressResponse saveShippingAddress(PrincipalDetails principalDetails, SaveShippingAddressRequest shippingAddressRequest) {
        User user = principalDetails.getUser();

        if (shippingAddressRequest.isDefault()) {
            shippingRepository.updateDefaultAddressToFalse(user.getId());
        }
        ShippingAddress shippingAddress = shippingAddressRequest.toEntity(user, shippingAddressRequest);
        ShippingAddress savedShippingAddress = shippingRepository.save(shippingAddress);
        return ShippingAddressResponse.from(savedShippingAddress);
    }

    @Transactional(readOnly = true)
    public List<ShippingAddressResponse> getShippingAddresses(Long userId) {
        List<ShippingAddress> addresses = shippingRepository.findAllByUserId(userId);
        return addresses.stream()
                .map(ShippingAddressResponse::from)
                .collect(Collectors.toList());
    }
}
