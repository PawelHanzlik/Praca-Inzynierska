package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.TripEntity;
import engineering.thesis.PSR.Entities.UserHistoryEntity;
import org.apache.catalina.User;

import java.util.List;

public interface UserHistoryService {
    List<UserHistoryEntity> getUserHistories(Long userId);
    void addHistory(UserHistoryEntity userHistory);
    void deleteHistory(UserHistoryEntity userHistory);
}
