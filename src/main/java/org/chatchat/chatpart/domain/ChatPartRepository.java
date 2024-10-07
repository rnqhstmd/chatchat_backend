package org.chatchat.chatpart.domain;

import org.chatchat.chatpart.domain.ChatPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatPartRepository extends JpaRepository<ChatPart, Long> {

}
