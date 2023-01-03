package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.UserHistoryEntity;
import engineering.thesis.PSR.Repositories.UserHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHistoryServiceImpl implements UserHistoryService{
    private UserHistoryRepository userHistoryRepository;

    public UserHistoryServiceImpl(UserHistoryRepository userHistoryRepository) {
        this.userHistoryRepository = userHistoryRepository;
    }

    @Override
    public List<UserHistoryEntity> getUserHistories(Long userId) {
        return userHistoryRepository.findUserHistoryEntitiesByUserId(userId);
    }

    @Override
    public void addHistory(UserHistoryEntity userHistory) {
        userHistoryRepository.save(userHistory);
    }

    @Override
    public void deleteHistory(UserHistoryEntity userHistory) {
        userHistoryRepository.delete(userHistory);
    }
}
