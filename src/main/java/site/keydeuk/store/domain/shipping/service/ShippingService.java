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
}
