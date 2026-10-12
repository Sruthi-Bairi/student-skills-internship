package com.ssims.repo;

import com.ssims.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

	List<Skill> findByProfileIdOrderByNameAsc(Long profileId);

	Optional<Skill> findByIdAndProfileId(Long id, Long profileId);
}
