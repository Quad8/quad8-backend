package site.keydeuk.store.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Notification;

@Repository
public interface AlarmRepositoy extends JpaRepository<Notification,Long> {

}
