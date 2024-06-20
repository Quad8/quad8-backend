package site.keydeuk.store.domain.communitylikes.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.community.repository.CommunityRepository;
import site.keydeuk.store.domain.communitylikes.repository.CommunityLikesRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.CommunityLikes;
import site.keydeuk.store.entity.User;

import static site.keydeuk.store.common.response.ErrorCode.*;
@RequiredArgsConstructor
@Service
public class CommunityLikesService {

    private final CommunityLikesRepository likesRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    @Transactional
    public CommunityLikes addLikes(Long userId, Long communityId){
        if (likesRepository.existsByUserIdAndCommunityId(userId,communityId)) throw new CustomException(ALREADY_EXIST_LIKE);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(()-> new CustomException(POST_NOT_FOUND));

        CommunityLikes likes = CommunityLikes.builder()
                .community(community)
                .user(user)
                .build();

        return likesRepository.save(likes);
    }

    @Transactional
    public void deleteLike(Long userId, Long communityId) {
        CommunityLikes likes = likesRepository.findByUserIdAndCommunityId(userId,communityId);
        if (likes== null) throw new CustomException(POST_NOT_FOUND);

        likesRepository.delete(likes);
    }

    @Transactional
    public void deleteAllLikesByCommunityId(Long communityId){
        likesRepository.deleteByCommunity_Id(communityId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndCommunityId(Long userId,Long communityId){
        return likesRepository.existsByUserIdAndCommunityId(userId,communityId);
    }

    @Transactional(readOnly = true)
    public Long countByCommunityId(Long communityId){
        return likesRepository.countByCommunity_Id(communityId);
    }
}
