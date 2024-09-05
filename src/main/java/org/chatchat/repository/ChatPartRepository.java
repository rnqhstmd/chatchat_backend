package org.chatchat.repository;

import org.chatchat.entity.ChatPart;
import org.chatchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatPartRepository extends JpaRepository<ChatPart, Long> {

}
